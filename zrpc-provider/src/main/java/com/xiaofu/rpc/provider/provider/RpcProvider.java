package com.xiaofu.rpc.provider.provider;

import com.xiaofu.rpc.codec.RpcDecoder;
import com.xiaofu.rpc.codec.RpcEncoder;
import com.xiaofu.rpc.core.RegistryService;
import com.xiaofu.rpc.core.RpcUtils;
import com.xiaofu.rpc.core.ServiceMeta;
import com.xiaofu.rpc.provider.annotation.RpcService;
import com.xiaofu.rpc.provider.cache.LocalCache;
import com.xiaofu.rpc.provider.handler.RpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author xiaofu
 * 1、启动本地服务[开启对客户端的监听]
 * 2、将服务发布到注册中心
 * @Date: 2021/4/22 15:25
 */
@Slf4j
public class RpcProvider implements InitializingBean, BeanPostProcessor{

    private RegistryService registryService;

    /**
     * 服务发布的端口
     */
    private Integer servicePort;

    /**
     * 服务发布的地址
     */
    private String serverAddr;




    public RpcProvider(RegistryService registryService, Integer servicePort) {
        this.registryService = registryService;
        this.servicePort = servicePort;
    }

    /**
     * 容器扩展点【启动服务】
     * afterPropertiesSet方法，初始化bean的时候执行，可以针对某个具体的bean进行配置,必须实现 InitializingBean接口
     * 基于容器扩展点，实现服务的发布启动
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //内部单独启动一个线程来发布服务
        new Thread(()->{
            try {
                startRpcServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * 启动服务，并开启端口监听客户端的请求
     */
    private void startRpcServer() throws UnknownHostException {
        //1.获取当前服务器的地址
        this.serverAddr = InetAddress.getLocalHost().getHostAddress();

        //采用Netty基于TCP协议启动服务
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        //添加自定义协议的编解码器
                        pipeline.addLast(new RpcEncoder());
                        pipeline.addLast(new RpcDecoder());
                        //添加自定义处理器，处理客户端的请求
                        pipeline.addLast(new RpcRequestHandler());
                    }

                })
                .childOption(ChannelOption.SO_KEEPALIVE,true);
        try {
            ChannelFuture future = serverBootstrap.bind(this.serverAddr, this.servicePort).sync();
            log.info("server start on {}:{}",this.serverAddr,this.servicePort);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 注册服务：所有的bean完成初始化之后，扫描Bean是否包含了@RpcService注解
     * 执行时机：bean完成初始化之后
     * postProcessAfterInitialization方法在bean初始化之后执行
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //1.扫描bean是否包含了@RpcService注解
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
        //2.如果包含，则构建出服务元信息，并将其保存到注册中心上
        if (rpcService != null){
            //3.构建ServiceMeta对象
            ServiceMeta serviceMeta = new ServiceMeta();
            serviceMeta.setServiceAddr(serverAddr);
            serviceMeta.setServicePort(servicePort);
            serviceMeta.setServiceName(rpcService.serviceInterface().getName());
            serviceMeta.setVersion(rpcService.version());
            try {
                //4.将服务注册到注册中心
                registryService.register(serviceMeta);
                //5.本地缓存保存一份
                LocalCache.rpcServiceMap.put(
                        RpcUtils.buildServiceKey(serviceMeta.getServiceName(),serviceMeta.getVersion()),
                        bean);
            } catch (Exception e) {
                e.printStackTrace();
                log.info("register service {} failed",serviceMeta.getServiceName());
            }
        }
        return null;
    }


}

















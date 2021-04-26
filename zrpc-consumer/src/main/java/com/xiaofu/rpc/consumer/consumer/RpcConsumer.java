package com.xiaofu.rpc.consumer.consumer;

import com.xiaofu.rpc.codec.RpcDecoder;
import com.xiaofu.rpc.codec.RpcEncoder;
import com.xiaofu.rpc.consumer.handler.RpcResponseHandler;
import com.xiaofu.rpc.core.RegistryService;
import com.xiaofu.rpc.core.RpcRequest;
import com.xiaofu.rpc.core.RpcUtils;
import com.xiaofu.rpc.core.ServiceMeta;
import com.xiaofu.rpc.protocol.RpcProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xiaofu
 * @Date: 2021/4/26 14:55
 */
@Slf4j
public class RpcConsumer {

    //基于Netty实现远程调用
    private Bootstrap bootstrap;
    private EventLoopGroup eventLoopGroup;

    public RpcConsumer(){
        bootstrap = new Bootstrap();
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new RpcEncoder())
                                .addLast(new RpcDecoder())
                                //TODO 添加自定义的处理逻辑，处理服务端的响应信息
                                .addLast(new RpcResponseHandler());
                    }
                });
    }

    /**
     *
     * @param protocol 自定义协议对象，封装了发送给服务端的消息
     * @param registryService 注册中心操作对象，方便我们获取到服务地址
     */
    public void sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService) throws Exception {
        //1.通过注册中心操作对象，获取到服务的对应的元数据
        //1.1 获取到ServiceKey
        RpcRequest request = protocol.getBody();
        String serviceKey = RpcUtils.buildServiceKey(request.getClassName(), request.getVersion());
        //1.2 获取到客户端的hashcode
        Object[] params = request.getParams();
        int hashcode = params != null ? params.hashCode() : serviceKey.hashCode();
        //1.3 通过注册中心操作对象，获取信息
        ServiceMeta serviceMeta = registryService.discovery(serviceKey, hashcode);

        //2.根据服务元数据的服务地址及端口，建立连接
        if (serviceMeta != null){
            ChannelFuture future = bootstrap.connect(serviceMeta.getServiceAddr(), serviceMeta.getServicePort()).sync();
            //通过监听器监控连接是否成功
            future.addListener((ChannelFutureListener) listener->{
               if (future.isSuccess()){
                   log.info("connect remote service {} on port {} success",serviceMeta.getServiceAddr(),serviceMeta.getServicePort());
               }else {
                   log.error("connect remote service {} on port {} failed",serviceMeta.getServiceAddr(),serviceMeta.getServicePort());
                   eventLoopGroup.shutdownGracefully();
               }
            });
            //向远程服务发送请求
            future.channel().writeAndFlush(protocol);
        }

    }
}
















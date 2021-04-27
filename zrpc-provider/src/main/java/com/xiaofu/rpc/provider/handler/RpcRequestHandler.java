package com.xiaofu.rpc.provider.handler;

import com.xiaofu.rpc.core.RpcRequest;
import com.xiaofu.rpc.core.RpcResponse;
import com.xiaofu.rpc.core.RpcUtils;
import com.xiaofu.rpc.protocol.MessageHeader;
import com.xiaofu.rpc.protocol.MessageStatusEnum;
import com.xiaofu.rpc.protocol.MessageTypeEnum;
import com.xiaofu.rpc.protocol.RpcProtocol;
import com.xiaofu.rpc.provider.cache.LocalCache;
import com.xiaofu.rpc.provider.processor.RpcRequestProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.reflect.FastClass;
import java.lang.reflect.InvocationTargetException;

/**
 * @author xiaofu
 * @Date: 2021/4/22 17:03
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequestProtocol) throws Exception {
        //RpcRequest:客户端发送的请求信息
        //RpcResponse:服务端返回的响应信息
        //RpcProtocol:自定义传输协议

        //将该业务操作提交给业务线程池进行处理
        RpcRequestProcessor.submit(()->{
            //1.创建一个返回给客户端的协议对象
            RpcProtocol<RpcResponse> rpcProtocol = new RpcProtocol<>();
            //2.设置响应体对象
            RpcResponse response = new RpcResponse();
            //3.设置响应体对象的报文类型
            MessageHeader header = rpcRequestProtocol.getHeader();
            header.setMsgType((byte) MessageTypeEnum.RESPONSE.getType());
            try {
                //4.根据客户端的请求信息，调用对应的服务方法，并返回结果
                Object result = handleRequest(rpcRequestProtocol.getBody());
                //5.设置响应的结果
                header.setStatus((byte) MessageStatusEnum.SUCCESS.getType());
                response.setData(result);
            } catch (Throwable throwable) {
                header.setStatus((byte) MessageStatusEnum.FAIL.getType());
                response.setMessage(throwable.toString());
                log.info("RpcRequestHandler process request {} fail",header.getMsgId());
            }
            //6.设置协议对象的头部及请求体
            rpcProtocol.setHeader(header);
            rpcProtocol.setBody(response);
            //给客户端返回处理结果
            channelHandlerContext.writeAndFlush(rpcProtocol);
        });
    }


    /**
     * 根据客户端的请求信息，调用对应的服务方法，并返回结果
     * @param request
     * @return
     */
    private Object handleRequest(RpcRequest request) throws InvocationTargetException {
        //根据请求的信息，定位到具体的方法上
        //1.构建serviceKey
        String serviceKey = RpcUtils.buildServiceKey(request.getClassName(), request.getVersion());
        //2.从本地缓存获取serviceKey对应的Bean
        Object serviceBean = LocalCache.rpcServiceMap.get(serviceKey);
        if (serviceBean == null){
            throw new RuntimeException("service not exist"+ request.getClassName() + ", method" + request.getMethodName());
        }
        //3.采用CGLIB提供的FastClass的方式来实现方法的调用
        //3.1 动态生成一个继承FastClass的类，并向这个类中写入委托对象
        FastClass fastClass = FastClass.create(serviceBean.getClass());
        //3.2 通过索引的方式定位到具体的方法
        int methodIndex = fastClass.getIndex(request.getMethodName(), request.getParameterTypes());
        //3.3 根据索引调用目标方法
        return fastClass.invoke(methodIndex,serviceBean,request.getParams());
    }


}

package com.xiaofu.rpc.consumer.consumer;

import com.xiaofu.rpc.core.RegistryService;
import com.xiaofu.rpc.core.RpcFuture;
import com.xiaofu.rpc.core.RpcRequest;
import com.xiaofu.rpc.core.RpcResponse;
import com.xiaofu.rpc.protocol.*;
import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.AllArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaofu
 * @Date: 2021/4/26 13:47
 */
@AllArgsConstructor
public class RpcInvokerProxy implements InvocationHandler {

    private RegistryService registryService;
    private String version;
    private long timeout;



    /**
     * 实现代理的处理逻辑
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //1.构建自定义协议包
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        //2.构建消息头
        MessageHeader header = new MessageHeader();
        header.setMagic(RpcProtocolConstants.MAGIC);
        header.setVersion(RpcProtocolConstants.VERSION);
        header.setSerialization((byte) RpcSerializationTypeEnum.HESSIAN.getType());
        header.setMsgType((byte) MessageTypeEnum.REQUEST.getType());
        header.setStatus((byte) MessageStatusEnum.SUCCESS.getType());
        //自增
        header.setMsgId(RpcRequestHolder.ID_GENERATOR.incrementAndGet());
        //3.构建消息体
        RpcRequest request = new RpcRequest();
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParams(args);
        //确定调用的服务的版本号
        request.setVersion(this.version);
        //4.设置消息头和消息体
        protocol.setHeader(header);
        protocol.setBody(request);
        //TODO
        //借助RpcConsumer来完成服务的真正调用
        RpcConsumer rpcConsumer = new RpcConsumer();
        rpcConsumer.sendRequest(protocol,registryService);
        //Future 异步保存响应结果
        RpcFuture<RpcResponse> future = new RpcFuture<>(
                new DefaultPromise<>(new DefaultEventLoop()), timeout);
        RpcRequestHolder.REQUEST_MAP.put(header.getMsgId(),future);
        //等待结果
        return future.getPromise().get(future.getTimeout(), TimeUnit.MILLISECONDS).getData();
    }
}




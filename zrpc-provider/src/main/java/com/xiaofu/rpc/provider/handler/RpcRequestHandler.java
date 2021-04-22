package com.xiaofu.rpc.provider.handler;

import com.xiaofu.rpc.core.RpcRequest;
import com.xiaofu.rpc.protocol.RpcProtocol;
import com.xiaofu.rpc.provider.processor.RpcRequestProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author xiaofu
 * @Date: 2021/4/22 17:03
 */
public class RpcRequestHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcRequest> rpcRequestProtocol) throws Exception {
        //RpcRequest:客户端发送的请求信息
        //RpcResponse:服务端返回的响应信息
        //RpcProtocol:自定义传输协议

        //将该业务操作提交给业务线程池进行处理
        RpcRequestProcessor.submit(()->{



        });

    }


}

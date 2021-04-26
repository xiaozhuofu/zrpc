package com.xiaofu.rpc.consumer.handler;

import com.xiaofu.rpc.consumer.consumer.RpcRequestHolder;
import com.xiaofu.rpc.core.RpcFuture;
import com.xiaofu.rpc.core.RpcRequest;
import com.xiaofu.rpc.core.RpcResponse;
import com.xiaofu.rpc.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author xiaofu
 * @Date: 2021/4/26 15:40
 */
public class RpcResponseHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {
        //接收到服务端的处理结果之后，将其结果信息设置到对应的future中
        long msgId = protocol.getHeader().getMsgId();
        RpcFuture<RpcResponse> future = RpcRequestHolder.REQUEST_MAP.get(msgId);
        future.getPromise().setSuccess(protocol.getBody());
    }



}

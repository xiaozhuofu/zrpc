package com.xiaofu.rpc.codec;

import com.xiaofu.rpc.core.RpcRequest;
import com.xiaofu.rpc.core.RpcResponse;
import com.xiaofu.rpc.protocol.MessageHeader;
import com.xiaofu.rpc.protocol.MessageTypeEnum;
import com.xiaofu.rpc.protocol.RpcProtocol;
import com.xiaofu.rpc.protocol.RpcProtocolConstants;
import com.xiaofu.rpc.serialization.RpcSerialization;
import com.xiaofu.rpc.serialization.factory.RpcSerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author xiaofu
 * @Description TODO
 * @Date: 2021/4/19 18:09
 */
public class RpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < RpcProtocolConstants.HEADER_TOTAL_LENGTH){
            return;
        }
        byteBuf.markReaderIndex();
        short magic = byteBuf.readShort();
        if (magic != RpcProtocolConstants.MAGIC){
            byteBuf.resetReaderIndex();
            throw new IllegalArgumentException("magic is illegal" + magic);
        }
        byte version = byteBuf.readByte();
        byte serialization = byteBuf.readByte();
        byte msgType = byteBuf.readByte();
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.findByType(msgType);
        if (messageTypeEnum == null){
            byteBuf.resetReaderIndex();
            throw new IllegalArgumentException("msgType is illegal" + msgType);
        }
        byte status = byteBuf.readByte();
        long msgId = byteBuf.readLong();
        int msgLength = byteBuf.readInt();
        if (byteBuf.readableBytes() < msgLength){
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] data = new byte[msgLength];
        byteBuf.readBytes(data);
        //构建header
        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serialization);
        header.setMsgType(msgType);
        header.setStatus(status);
        header.setMsgId(msgId);
        header.setMsgLength(msgLength);
        //body
        RpcSerialization rpcSerialization = RpcSerializationFactory.getRpcSerialization(serialization);
        switch (messageTypeEnum){
            case REQUEST:
                RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
                requestRpcProtocol.setHeader(header);
                RpcRequest request = rpcSerialization.deserialize(data, RpcRequest.class);
                requestRpcProtocol.setBody(request);
                list.add(requestRpcProtocol);
                break;
            case RESPONSE:
                RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();
                responseRpcProtocol.setHeader(header);
                RpcResponse response = rpcSerialization.deserialize(data, RpcResponse.class);
                responseRpcProtocol.setBody(response);
                list.add(responseRpcProtocol);
                break;
        }
    }
}

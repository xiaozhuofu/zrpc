package com.xiaofu.rpc.codec;

import com.xiaofu.rpc.protocol.MessageHeader;
import com.xiaofu.rpc.protocol.RpcProtocol;
import com.xiaofu.rpc.protocol.RpcSerializationTypeEnum;
import com.xiaofu.rpc.serialization.RpcSerialization;
import com.xiaofu.rpc.serialization.factory.RpcSerializationFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author xiaofu
 * @Description TODO
 * @Date: 2021/4/19 18:01
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol<Object> objectRpcProtocol, ByteBuf byteBuf) throws Exception {
        MessageHeader header = objectRpcProtocol.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getSerialization());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getMsgId());

        //序列化操作
        RpcSerialization rpcSerialization = RpcSerializationFactory.getRpcSerialization(header.getSerialization());
        byte[] data = rpcSerialization.serialize(objectRpcProtocol.getBody());
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}

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
        //从协议包对象中获得协议头对象
        MessageHeader header = objectRpcProtocol.getHeader();
        //将协议头的信息写入ByteBuf
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getSerialization());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getMsgId());
        //序列化操作,将协议体对象转换为数组，消息的长度由它决定
        //协议头确定序列化算法
        RpcSerialization rpcSerialization = RpcSerializationFactory.getRpcSerialization(header.getSerialization());
        byte[] data = rpcSerialization.serialize(objectRpcProtocol.getBody());
        //将协议体的消息长度及内容信息写入ByteBuf
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}

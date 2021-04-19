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
        //边界判断，是否为一个合法的消息
        if (byteBuf.readableBytes() < RpcProtocolConstants.HEADER_TOTAL_LENGTH){
            //规定的自定义协议包中，协议头的长度是HEADER_TOTAL_LENGTH，没有达到则证明不是一个合法消息
            return;
        }
        //记录当前的读索引位置s
        byteBuf.markReaderIndex();
        //判断魔数是否合法
        short magic = byteBuf.readShort();
        if (magic != RpcProtocolConstants.MAGIC){
            byteBuf.resetReaderIndex();
            throw new IllegalArgumentException("magic is illegal" + magic);
        }
        //读取协议头其他信息
        byte version = byteBuf.readByte();
        byte serialization = byteBuf.readByte();
        byte msgType = byteBuf.readByte();
        //判断消息类型是否合法
        MessageTypeEnum messageTypeEnum = MessageTypeEnum.findByType(msgType);
        if (messageTypeEnum == null){
            byteBuf.resetReaderIndex();
            throw new IllegalArgumentException("msgType is illegal" + msgType);
        }
        byte status = byteBuf.readByte();
        long msgId = byteBuf.readLong();
        int msgLength = byteBuf.readInt();
        //判断消息是否传输完毕
        if (byteBuf.readableBytes() < msgLength){
            byteBuf.resetReaderIndex();
            return;
        }
        //读取消息体
        byte[] data = new byte[msgLength];
        byteBuf.readBytes(data);
        //完成数据读取，构建一个对象,header
        MessageHeader header = new MessageHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSerialization(serialization);
        header.setMsgType(msgType);
        header.setStatus(status);
        header.setMsgId(msgId);
        header.setMsgLength(msgLength);
        //构建协议体内容, body
        RpcSerialization rpcSerialization = RpcSerializationFactory.getRpcSerialization(serialization);
        switch (messageTypeEnum){
            //当前发送的是请求包
            case REQUEST:
                RpcRequest request = rpcSerialization.deserialize(data, RpcRequest.class);
                if (request != null){
                    RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(request);
                    list.add(protocol);
                }
                break;
            //当前发送的是响应包
            case RESPONSE:
                RpcResponse response = rpcSerialization.deserialize(data, RpcResponse.class);
                if (response != null){
                    RpcProtocol<RpcResponse> protocol = new RpcProtocol<>();
                    protocol.setHeader(header);
                    protocol.setBody(response);
                    list.add(protocol);
                }
                break;
        }
    }
}

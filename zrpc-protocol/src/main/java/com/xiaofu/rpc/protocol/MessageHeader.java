package com.xiaofu.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaofu
 * @Description TODO
 * @createTime 2021年04月18日 11:57:00
 */
@Data
public class MessageHeader implements Serializable {

    /**
     * 魔数 2byte
     * 协议版本号 1byte
     * 序列化算法类型 1byte
     * 报文类型 1byte
     * 状态 1byte
     * 消息ID 8byte
     * 数据长度 4byte
     */
    private Short magic;
    private byte version;
    private byte serialization;
    private byte msgType;
    private byte status;
    private int msgLength;
}




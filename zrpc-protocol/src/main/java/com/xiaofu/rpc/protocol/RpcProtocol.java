package com.xiaofu.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaofu
 * @Description  协议包完整类
 * @Date: 2021/4/19 16:49
 */
@Data
public class RpcProtocol<T> implements Serializable {

    private  MessageHeader header;

    private T body;

}

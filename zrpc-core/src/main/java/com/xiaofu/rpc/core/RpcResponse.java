package com.xiaofu.rpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaofu
 * @Description  分装服务端的响应信息
 * 响应存在两种可能
 * 1、成功->data
 * 2、失败->message
 * @createTime 2021年04月17日 19:21:00
 */
@Data
public class RpcResponse implements Serializable {

    /**
     * 用于封装正确响应时的返回数据
     */
    private Object data;

    /**
     * 用于封装响应失败时，返回的错误日志信息
     */
    private String message;

}

package com.xiaofu.rpc.serialization.exception;

/**
 * @author xiaofu
 * @Description  自定义异常是框架的常用做法，通常是运行时异常，一般用来描述业务逻辑错误
 * @Date: 2021/4/19 17:17
 */
public class RpcSerializationException extends RuntimeException {

    private static final long serialVersionUID = 6666L;


    public RpcSerializationException(String msg){
        super(msg);
    }

    public RpcSerializationException(Throwable cause){
        super(cause);
    }

}

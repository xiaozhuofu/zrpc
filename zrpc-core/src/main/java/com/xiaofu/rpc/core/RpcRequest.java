package com.xiaofu.rpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaofu
 * @Description TODO 封装客户端给服务端发送的远程调用请求信息
 * @createTime 2021年04月17日 19:14:00
 */
@Data
public class RpcRequest implements Serializable {

    /**
     * 调用的类名
     */
    private String className;

    /**
     * 调用的方法名
     */
    private String methodName;

    /**
     * 方法的参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 方法的实际参数值
     */
    private Object[] params;

    /**
     * 服务的版本
     */
    private String version;

}

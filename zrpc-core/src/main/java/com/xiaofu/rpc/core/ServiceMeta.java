package com.xiaofu.rpc.core;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xiaofu
 * @Description  服务的元信息
 * 我们将服务发布到注册中心，用来描述当前服务状态的
 * @createTime 2021年04月17日 19:23:00
 */
@Data
public class ServiceMeta implements Serializable {

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务版本号
     */
    private String version;

    /**
     * 服务地址
     */
    private String serviceAddr;

    /**
     * 服务端口
     */
    private Integer servicePort;


}

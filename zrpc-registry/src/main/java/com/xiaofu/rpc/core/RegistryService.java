package com.xiaofu.rpc.core;

/**
 * @author xiaofu
 * @Description   定义操作注册中心的服务接口
 * 1、register 服务注册
 * 2、unRegister 服务注销
 * 3、discovery 服务发现
 * @Date: 2021/4/20 12:19
 */
public interface RegistryService {


    /**
     * 服务注册
     * @param serviceMeta 服务元数据信息
     * @throws Exception
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务注销
     * @param serviceMeta 服务元数据信息
     * @throws Exception
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;


    /**
     * 根据服务名称，进行服务发现
     * @param serviceKey 服务名称
     * @param hashcode  客户端hashcode
     * @return
     * @throws Exception
     */
    ServiceMeta discovery(String serviceKey,int hashcode) throws Exception;

}


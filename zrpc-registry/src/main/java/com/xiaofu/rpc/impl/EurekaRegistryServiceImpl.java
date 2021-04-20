package com.xiaofu.rpc.impl;

import com.xiaofu.rpc.core.RegistryService;
import com.xiaofu.rpc.core.ServiceMeta;

/**
 * @author xiaofu
 * @Description TODO 基于Eureka实现的服务注册中心实现类
 * @Date: 2021/4/20 16:24
 */
public class EurekaRegistryServiceImpl implements RegistryService {
    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {

    }

    @Override
    public ServiceMeta discovery(String serviceKey, int hashcode) throws Exception {
        return null;
    }
}

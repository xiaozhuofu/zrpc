package com.xiaofu.rpc.factory;

import com.xiaofu.rpc.core.RegistryService;
import com.xiaofu.rpc.core.RegistryTypeEnum;
import com.xiaofu.rpc.impl.EurekaRegistryServiceImpl;
import com.xiaofu.rpc.impl.ZookeeperRegistryServiceImpl;

/**
 * @author xiaofu
 * @Description TODO 注册中心工厂类
 * @Date: 2021/4/20 16:47
 */
public class RegistryServiceFactory {

    /**
     * 注册中心实例
     */
    private static volatile RegistryService registryService;

    /**
     * 根据注册中心类型，获得注册中心实例
     * @param registryAddr 注册中心地址
     * @param type 注册中心类型
     * @return
     * @throws Exception
     */
    public static RegistryService getInstance(String registryAddr, RegistryTypeEnum type) throws Exception {
        //双重检测
        if (registryService == null){
            synchronized (RegistryServiceFactory.class){
                if (registryService == null){
                    switch (type){
                        case ZOOKEEPER:
                            registryService = new ZookeeperRegistryServiceImpl(registryAddr);
                            break;
                        case EUREKA:
                            registryService = new EurekaRegistryServiceImpl();
                            break;
                        default:
                            throw new IllegalArgumentException("registry type is illegal" + type);
                    }

                }
            }
        }
        return registryService;
    }


}

package com.xiaofu.rpc.provider.configuration;

import com.xiaofu.rpc.core.RegistryService;
import com.xiaofu.rpc.core.RegistryTypeEnum;
import com.xiaofu.rpc.factory.RegistryServiceFactory;
import com.xiaofu.rpc.provider.properties.RpcProperties;
import com.xiaofu.rpc.provider.provider.RpcProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author xiaofu
 * @Description
 * @Date: 2021/4/22 15:59
 */
@Configuration
@EnableConfigurationProperties(RpcProperties.class)
public class RpcProviderConfiguration {

    @Resource
    private RpcProperties rpcProperties;


    @Bean
    public RpcProvider initRpcProvider() throws Exception {
        //1.获取注册中心类型
        RegistryTypeEnum registryType = RegistryTypeEnum.valueOf(rpcProperties.getRegistryType());
        //2.获取注册中心的地址
        String registryAddr = rpcProperties.getRegistryAddr();
        //3.根据类型及地址，获取到操作注册中心的具体实例对象
        RegistryService registryService = RegistryServiceFactory.getInstance(registryAddr, registryType);
        return new RpcProvider(registryService,rpcProperties.getServicePort());
    }


}









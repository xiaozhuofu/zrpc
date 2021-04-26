package com.xiaofu.rpc.consumer.consumer;

import com.xiaofu.rpc.core.RegistryService;
import com.xiaofu.rpc.core.RegistryTypeEnum;
import com.xiaofu.rpc.factory.RegistryServiceFactory;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

/**
 * @author xiaofu
 * @Date: 2021/4/26 10:49
 */
public class RpcReferenceBean implements FactoryBean<Object> {

    @Setter
    private Class<?> interfaceClass;

    @Setter
    private String registryType;

    @Setter
    private String registryAddr;

    @Setter
    private String version;

    @Setter
    private long timeout;

    private Object bean;


    /**
     * 返回实例化后的具体bean
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject() throws Exception {
        return bean;
    }

    /**
     * 返回bean的类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        return interfaceClass;
    }

    /**
     * 完成object对象的实例化,生成一个动态的代理对象
     * @throws Exception
     */
    public void init() throws Exception{
        //1.获取注册中心的操作实例化对象
        RegistryService registryService = RegistryServiceFactory.getInstance(this.registryAddr, RegistryTypeEnum.valueOf(this.registryType));
        //2.构建代理对象
        this.bean = Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                //实现代理逻辑,并且将注解上的version和timeout传递下去
                new RpcInvokerProxy(registryService,version,timeout));
    }
}

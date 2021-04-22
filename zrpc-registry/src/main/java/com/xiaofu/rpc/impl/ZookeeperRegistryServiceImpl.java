package com.xiaofu.rpc.impl;

import com.xiaofu.rpc.core.RegistryService;
import com.xiaofu.rpc.core.RpcUtils;
import com.xiaofu.rpc.core.ServiceMeta;
import com.xiaofu.rpc.loadbalancer.impl.ZookeeperServiceLoadBalancerImpl;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;

/**
 * @author xiaofu
 * @Description  基于zookeeper实现操作注册中心的服务实现类
 * @Date: 2021/4/20 15:39
 */
public class ZookeeperRegistryServiceImpl implements RegistryService {


    private static final String ZK_BASE_PATH = "zrpc";

    /**
     * 实现服务注册与发现的工具类
     */
    private ServiceDiscovery<ServiceMeta> serviceDiscovery;


    /**
     * 注册中心服务实现类构造方法
     * @param registryAddr 注册中心服务地址
     * @throws Exception
     */
    public ZookeeperRegistryServiceImpl(String registryAddr) throws Exception {
        //1.创建一个操作zookeeper客户端，并且设置重试策略
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(1000, 3));
        client.start();
        //2.初始化服务发现对象实例
        //创建一个序列化对象
        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);
        serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();
        serviceDiscovery.start();
    }


    /**
     * 服务注册
     * @param serviceMeta 服务元数据信息
     * @throws Exception
     */
    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = getServiceInstance(serviceMeta);
        serviceDiscovery.registerService(serviceInstance);
    }

    /**
     * 服务注销
     * @param serviceMeta 服务元数据信息
     * @throws Exception
     */
    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {
        ServiceInstance<ServiceMeta> serviceInstance = getServiceInstance(serviceMeta);
        serviceDiscovery.unregisterService(serviceInstance);
    }

    /**
     * 根据服务名称，进行服务发现
     * @param serviceKey 服务名称
     * @param hashcode  客户端hashcode
     * @return
     * @throws Exception
     */
    @Override
    public ServiceMeta discovery(String serviceKey, int hashcode) throws Exception {
        //1.根据serviceKey获取到对应的服务列表
        Collection<ServiceInstance<ServiceMeta>> serviceInstances = serviceDiscovery.queryForInstances(serviceKey);
        //2.根据负载均衡实现逻辑，得到对应服务节点
        ServiceInstance<ServiceMeta> serviceInstance = new ZookeeperServiceLoadBalancerImpl().
                select((List<ServiceInstance<ServiceMeta>>) serviceInstances, hashcode);
        //3.获取服务实例保存的服务元数据信息
        if (serviceInstance != null){
            return serviceInstance.getPayload();
        }
        return null;
    }


    /**
     * 获取服务实例
     * @param serviceMeta
     * @return
     * @throws Exception
     */
    private ServiceInstance<ServiceMeta> getServiceInstance(ServiceMeta serviceMeta) throws Exception {
        return ServiceInstance.<ServiceMeta>builder()
                .name(RpcUtils.buildServiceKey(serviceMeta.getServiceName(), serviceMeta.getVersion()))
                .address(serviceMeta.getServiceAddr())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();
    }
}

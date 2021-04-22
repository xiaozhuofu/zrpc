package com.xiaofu.rpc.loadbalancer.impl;

import com.xiaofu.rpc.core.ServiceMeta;
import com.xiaofu.rpc.loadbalancer.ServiceLoadBalancer;
import org.apache.curator.x.discovery.ServiceInstance;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author xiaofu
 * @Description  基于zookeeper的负载均衡实现
 * @Date: 2021/4/20 14:59
 */
public class ZookeeperServiceLoadBalancerImpl implements ServiceLoadBalancer<ServiceInstance<ServiceMeta>> {

    /**
     * 每个服务虚拟节点的个数
     */
    private static final int virtual_node_size = 2;


    /**
     * 基于一致性hash算法，在服务列表中选择一个合适的节点
     * @param servers 服务列表
     * @param hashcode 客户端hashcode
     * @return
     */
    @Override
    public ServiceInstance<ServiceMeta> select(List<ServiceInstance<ServiceMeta>> servers, int hashcode) {
        //将服务列表通过一致性Hash算法构成一个环
        TreeMap<Integer,ServiceInstance<ServiceMeta>> ring = buildConsistencyHashRing(servers);
        //根据客户端的hashcode值获得服务的实例
        return assignServerNode(hashcode, ring);
    }

    /**
     * 根据客户端的hashcode值获得服务的实例
     * @param hashcode 客户端hashcode
     * @param ring 一致性hash环
     * @return
     */
    private ServiceInstance<ServiceMeta> assignServerNode(int hashcode, TreeMap<Integer, ServiceInstance<ServiceMeta>> ring) {
        Map.Entry<Integer, ServiceInstance<ServiceMeta>> instanceEntry = ring.ceilingEntry(hashcode);
        if (instanceEntry != null){
            return instanceEntry.getValue();
        }
        return null;
    }

    /**
     * 将服务列表通过一致性Hash算法构成一个环
     * @param servers 服务列表
     * @return
     */
    private TreeMap<Integer, ServiceInstance<ServiceMeta>> buildConsistencyHashRing(List<ServiceInstance<ServiceMeta>> servers) {
        TreeMap<Integer,ServiceInstance<ServiceMeta>> ring = new TreeMap<>();
        //每个服务构建对应的虚拟节点，并保存到环中
        for (ServiceInstance<ServiceMeta> server : servers) {
            String serviceKey = buildServiceInstanceKey(server);
            //构建一个服务计算hash值的组合
            for (int i = 0; i < virtual_node_size; i++) {
                ring.put((serviceKey+i).hashCode(),server);
            }
        }
        return ring;
    }

    /**
     * 根据服务的元信息构建一个描述服务的key
     * @param server 单个服务
     * @return
     */
    private String buildServiceInstanceKey(ServiceInstance<ServiceMeta> server) {
        ServiceMeta serviceMeta = server.getPayload();
        StringBuilder stringBuilder = new StringBuilder(serviceMeta.getServiceAddr());
        stringBuilder.append(":");
        stringBuilder.append(serviceMeta.getServicePort());
        return stringBuilder.toString();
    }

}

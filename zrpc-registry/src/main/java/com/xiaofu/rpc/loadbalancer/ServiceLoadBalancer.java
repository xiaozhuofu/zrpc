package com.xiaofu.rpc.loadbalancer;

import java.util.List;

/**
 * @author xiaofu
 * @Description TODO 定义负载均衡实现接口
 * 1、未来支持多负载均衡实现接口
 * @Date: 2021/4/20 12:30
 */
public interface ServiceLoadBalancer<T> {


    /**
     * 基于一致性hash算法，在服务列表中选择一个合适的节点
     * @param servers 服务列表
     * @param hashcode 客户端hashcode
     * @return
     */
    T select(List<T> servers, int hashcode);


}

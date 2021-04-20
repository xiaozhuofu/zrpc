package com.xiaofu.rpc.loadbalancer.impl;

import com.xiaofu.rpc.loadbalancer.ServiceLoadBalancer;

import java.util.List;

/**
 * @author xiaofu
 * @Description TODO 基于Eureka的负载均衡实现
 * @Date: 2021/4/20 16:23
 */
public class EurekaServiceLoadBalancerImpl implements ServiceLoadBalancer<Object> {
    @Override
    public Object select(List<Object> servers, int hashcode) {
        return null;
    }
}

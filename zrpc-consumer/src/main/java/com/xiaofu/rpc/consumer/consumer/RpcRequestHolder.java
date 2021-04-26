package com.xiaofu.rpc.consumer.consumer;

import com.xiaofu.rpc.core.RpcFuture;
import com.xiaofu.rpc.core.RpcResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xiaofu
 * 全局变量的管理器
 * @Date: 2021/4/26 14:46
 */
public class RpcRequestHolder {

    /**
     * 全局消息ID生成器
     */
    public static final AtomicLong ID_GENERATOR = new AtomicLong(0);

    /**
     * 全局的请求处理结果容器
     */
    public static final Map<Long, RpcFuture<RpcResponse>> REQUEST_MAP = new ConcurrentHashMap<>();


}

package com.xiaofu.rpc.provider.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaofu
 * 本地缓存定义，用于保存服务key和服务
 * @Date: 2021/4/22 16:50
 */
public class LocalCache {

    public static final Map<String, Object> rpcServiceMap = new ConcurrentHashMap<>();

}

package com.xiaofu.rpc.provider.processor;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaofu
 * 为业务请求处理分配业务线程
 * @Date: 2021/4/22 16:59
 */
public class RpcRequestProcessor {

    private static volatile ThreadPoolExecutor threadPool;

    public static void submit(Runnable task){
        //采用双重检测机制，完成线程池的初始化
        if (threadPool == null){
            synchronized (RpcRequestProcessor.class){
                if (threadPool == null){
                    threadPool = new ThreadPoolExecutor(8,8,30, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<>(1000));
                }
            }
        }
        threadPool.submit(task);
    }


}

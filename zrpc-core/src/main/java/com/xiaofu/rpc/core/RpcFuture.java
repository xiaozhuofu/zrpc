package com.xiaofu.rpc.core;

import io.netty.util.concurrent.Promise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xiaofu
 * @Description TODO 自定义异步调用的返回接口
 * @createTime 2021年04月17日 19:27:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcFuture<T> implements Serializable {

    /**
     * 接收异步调用的返回结果
     */
    private Promise<T> promise;

    /**
     * 设置异步调用的超时时间
     */
    private long timeout;


}

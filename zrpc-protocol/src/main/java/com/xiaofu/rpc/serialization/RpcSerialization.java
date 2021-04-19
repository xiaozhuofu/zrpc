package com.xiaofu.rpc.serialization;

import java.io.IOException;

/**
 * @author xiaofu
 * @Description TODO 定义序列化接口
 * @Date: 2021/4/19 17:12
 */
public interface RpcSerialization {


    /**
     * 序列化，将对象转换为字节数组
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> byte[] serialize(T obj) throws IOException;

    /**
     * 反序列化，将字节数组转化为具体的对象
     * @param data
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    <T> T deserialize(byte[] data, Class<T> clazz) throws IOException;

}

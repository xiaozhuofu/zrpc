package com.xiaofu.rpc.serialization.impl;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.xiaofu.rpc.serialization.RpcSerialization;
import com.xiaofu.rpc.serialization.exception.RpcSerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author xiaofu
 * @Description TODO 基于Hessian实现序列化  跟JDK差不多
 * @Date: 2021/4/19 17:20
 */
public class HessianSerializationImpl implements RpcSerialization {


    /**
     * 序列化，将对象转换为字节数组
     * @param obj
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> byte[] serialize(T obj) throws IOException {
        //1.边界判断
        if (obj == null){
            throw new NullPointerException();
        }
        //2.序列化操作
        byte[] result;
        //创建输出对象
        HessianSerializerOutput hessianSerializerOutput;
        //内存流 ByteArrayOutputStream 写到内存中
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            hessianSerializerOutput = new HessianSerializerOutput(outputStream);
            //写入对象，并将其存储到对应的内存流中
            hessianSerializerOutput.writeObject(obj);
            hessianSerializerOutput.flush();
            //从内存流中获取到对应的字节数组
            result = outputStream.toByteArray();
        }catch (Exception e){
            throw new RpcSerializationException(e);
        }
        return result;
    }

    /**
     * 反序列化，将字节数组转化为具体的对象
     * @param data
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws IOException {
        //1.边界判断
        if (data == null){
            throw new NullPointerException();
        }
        //2.反序列化操作
        T result;
        HessianSerializerInput hessianSerializerInput;
        try(ByteArrayInputStream inputStream = new ByteArrayInputStream(data)){
            hessianSerializerInput = new HessianSerializerInput(inputStream);
            result= (T) hessianSerializerInput.readObject(clazz);
        }catch (Exception e){
            throw new RpcSerializationException(e);
        }
        return result;
    }


}

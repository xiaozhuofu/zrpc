package com.xiaofu.rpc.serialization.factory;

import com.xiaofu.rpc.protocol.RpcSerializationTypeEnum;
import com.xiaofu.rpc.serialization.RpcSerialization;
import com.xiaofu.rpc.serialization.impl.HessianSerializationImpl;

/**
 * @author xiaofu
 * @Description TODO 序列化工厂类，根据协议头部的序列化类型字段，返回具体的序列化实现类
 * @Date: 2021/4/19 17:40
 */
public class RpcSerializationFactory {

    /**
     * 获得序列化实例对象
     * @param type 序列化类型参数type
     * @return
     */
     public static RpcSerialization getRpcSerialization(int type){
         //根据type获得对应的枚举类型
         RpcSerializationTypeEnum serializationType = RpcSerializationTypeEnum.findByType(type);
         //根据枚举类型，返回对应的序列化实现类对象
         switch (serializationType){
             case HESSIAN:
                 return new HessianSerializationImpl();
             default:
                 throw  new IllegalArgumentException("serialization type illegal"+ type);
         }
     }


}

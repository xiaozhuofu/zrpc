package com.xiaofu.rpc.protocol;

import lombok.Getter;

/**
 * @author xiaofu
 * @Description
 * @createTime 2021年04月18日 17:19:00
 */
public enum RpcSerializationTypeEnum {

    HESSIAN(1),
    PROTOBUF(2);


    @Getter
    private int type;

    RpcSerializationTypeEnum(int type){
        this.type = type;
    }

    /**
     * 根据类型字段，获得对应的枚举
     * @param type
     * @return
     */
    public static RpcSerializationTypeEnum findByType(int type){
        for (RpcSerializationTypeEnum typeEnum : RpcSerializationTypeEnum.values()) {
            if (typeEnum.getType() == type){
                return typeEnum;
            }
        }
        //框架默认采用的序列化算法为HESSIAN
        return HESSIAN;
    }

}

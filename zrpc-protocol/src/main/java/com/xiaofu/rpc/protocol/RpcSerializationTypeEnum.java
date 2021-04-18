package com.xiaofu.rpc.protocol;

import lombok.Getter;

/**
 * @author xiaofu
 * @Description TODO
 * @createTime 2021年04月18日 17:19:00
 */
public enum RpcSerializationTypeEnum {

    @Getter
    private int type;

    RpcSerializationTypeEnum(int type){
        this.type = type;
    }



}

package com.xiaofu.rpc.protocol;

import lombok.Getter;

/**
 * @author xiaofu
 * @Description TODO 定义消息状态枚举类型
 * @Date: 2021/4/19 16:41
 */
public enum MessageStatusEnum {

    SUCCESS(1),
    FAIL(0);


    @Getter
    private int type;

    MessageStatusEnum(int type){
        this.type = type;
    }

}

package com.xiaofu.rpc.protocol;

import lombok.Getter;

/**
 * @author xiaofu
 * @Description TODO 定义消息类型
 * @Date: 2021/4/19 16:35
 */
public enum MessageTypeEnum {

    REQUEST(1),
    RESPONSE(2);


    @Getter
    private int type;

    MessageTypeEnum(int type){
        this.type = type;
    }

    /**
     * 根据协议头的字段的数值，来定位到具体的消息类型
     * @param type
     * @return
     */
    public static MessageTypeEnum findByType(int type){
        for (MessageTypeEnum typeEnum : MessageTypeEnum.values()) {
            if (typeEnum.getType() == type){
                return typeEnum;
            }
        }
        return null;
    }

}

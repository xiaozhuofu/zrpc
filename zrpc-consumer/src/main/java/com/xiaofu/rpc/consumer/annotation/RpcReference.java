package com.xiaofu.rpc.consumer.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;

/**
 * @author xiaofu
 * @Date: 2021/4/23 15:10
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Autowired
public @interface RpcReference {

    String registryType() default "ZOOKEEPER";

    String registryAddr() default "127.0.0.1:1281";

    String version() default "1.0.0";

    long timeout() default 3000;

}

package com.xiaofu.rpc.provider.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xiaofu
 * @Description  定义服务发布的注解类
 * @Retention(RetentionPolicy.RUNTIME) 表示该注解在运行期有效
 * @Target(ElementType.TYPE) 表示该注解可以加载类描述上
 * @Date: 2021/4/22 14:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RpcService {

    /**
     * 表示其描述服务类型
     * @return
     */
    Class<?> serviceInterface() default Object.class;

    /**
     * 表示服务的版本号
     * @return
     */
    String version() default "1.0.0";

}



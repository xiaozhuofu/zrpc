package com.xiaofu.rpc.core;

/**
 * @author xiaofu
 * @Description TODO RPC服务工具类
 * @createTime 2021年04月17日 19:32:00
 */
public class RpcUtils {

    /**
     * 根据服务名称及服务的版本号，构建一个服务的唯一标识  userService#1.0.0
     * @param serviceName
     * @param version
     * @return
     */
    public static String buildServiceKey(String serviceName, String version){
        StringBuilder stringBuilder = new StringBuilder(serviceName);
        stringBuilder.append("#");
        stringBuilder.append(version);
        return stringBuilder.toString();
    }


}

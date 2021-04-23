package com.xiaofu.rpc.provider.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author xiaofu
 * @Description
 * @Date: 2021/4/22 15:18
 */
@Data
@ConfigurationProperties(prefix = "zrpc")
public class RpcProperties {

    private String registryType;
    private String registryAddr;
    private Integer servicePort;

}

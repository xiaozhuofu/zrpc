package com.xiaofu.rpc.provider.service;

import com.xiaofu.rpc.UserService;
import com.xiaofu.rpc.provider.annotation.RpcService;

/**
 * @author xiaofu
 * @Description
 * @Date: 2021/4/22 15:14
 */
@RpcService(serviceInterface = UserService.class,version = "1.0.0")
public class UserServiceImpl implements UserService {
    @Override
    public String hello() {
        return "hello, zrpc";
    }
}

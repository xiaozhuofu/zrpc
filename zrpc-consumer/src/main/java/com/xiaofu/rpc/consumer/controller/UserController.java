package com.xiaofu.rpc.consumer.controller;

import com.xiaofu.rpc.UserService;
import com.xiaofu.rpc.consumer.annotation.RpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaofu
 * @Date: 2021/4/26 10:45
 */
@RestController
public class UserController {

    @RpcReference(registryAddr = "49.234.73.161:2184",timeout = 5000)
    private UserService userService;

    @GetMapping("/hello")
    public String hello(){
        return userService.hello();
    }


}

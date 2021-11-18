package com.dubbo.controller;

import com.dubbo.api.UserService;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangyongwen
 * @date 2021/11/17
 * @Description
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Value("${spring.application.name}")
    String applicationName;


    @DubboReference
    private UserService userService;


    @RequestMapping("{id}")
    public User getUser(@PathVariable("id") Long id) {

        log.info("applicationName:{}",applicationName);
        User user = userService.getUserInfo(id);
        log.info("response from provider: {}" , user);

        return user;
    }

}

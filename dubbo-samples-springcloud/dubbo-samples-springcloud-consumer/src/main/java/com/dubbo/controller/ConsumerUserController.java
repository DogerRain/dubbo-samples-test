package com.dubbo.controller;

import com.dubbo.api.UserService;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/17
 * @Description
 */
@RestController
@RequestMapping("/consumer")
@Slf4j
public class ConsumerUserController {

    @DubboReference(version = "*", protocol = "dubbo,hessian", loadbalance = "random" )
    private UserService userService;

//    @DubboReference(version = "*",protocol = "dubbo")
//    private UserService userService2;

    @RequestMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        User user = userService.getUserInfo(id);
        log.info("response from provider: {}", user);
        return user;
    }

    @RequestMapping("/userHessian/{id}")
    public User getUserHessian(@PathVariable("id") Long id) {
//        User user = userService2.getUserInfo(id);
//        log.info("response from provider: {}", user);
//        return user;
        return null;
    }


}

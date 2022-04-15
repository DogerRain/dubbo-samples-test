package com.dubbo.controller;

import com.dubbo.api.UserService;
import com.dubbo.service.UserServiceImpl2;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/19
 * @Description
 */
@RestController
@RequestMapping("/provider")
@Slf4j
public class ProviderUserController {
//
//    @Resource
//    UserServiceImpl userService;

//      @Resource(name = "UserServiceImpl2")
//      UserService userServiceImpl2;

//    @Qualifier("userServiceImpl2") //报错，没有这个userServiceImpl2
//    @Autowired
//    UserService userServiceImpl22;

    @Qualifier("UserServiceImpl2")
    @Autowired
    UserService userServiceImpl222;

//    @Autowired
//    UserService UserServiceImpl2;

    @Autowired
    UserServiceImpl2 userServiceImpl2;


    @RequestMapping("/user/{id}")
    User getUserInfo(@PathVariable("id") Long id ){
        log.debug("我是controller下的debug");
        log.warn("我是controller下的warn");
        log.error("我是controller下的error");

        userServiceImpl2.getUserInfo(1);

//        return userService.getUserInfo(id);
        return null;
    }

}

package com.dubbo.controller;

import com.dubbo.service.UserServiceImpl;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

    @Resource
    UserServiceImpl userService;

    @RequestMapping("/user/{id}")
    User getUserInfo(@PathVariable("id") Long id ){
        log.debug("我是controller下的debug");
        log.warn("我是controller下的warn");
        log.error("我是controller下的error");

        return userService.getUserInfo(id);
    }

}

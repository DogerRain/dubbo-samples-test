package com.dubbo.controller;

import com.dubbo.service.UserServiceImpl;
import com.dubbo.vo.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author huangyongwen
 * @date 2021/11/19
 * @Description
 */
@RestController
@RequestMapping("/provider")
public class ProviderUserController {

    @Resource
    UserServiceImpl userService;

    @RequestMapping("/user/{id}")
    User getUserInfo(@PathVariable("id") Long id ){
        return userService.getUserInfo(id);
    }

}

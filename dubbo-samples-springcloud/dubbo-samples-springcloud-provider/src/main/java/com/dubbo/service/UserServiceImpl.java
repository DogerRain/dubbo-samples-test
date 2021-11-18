package com.dubbo.service;

import com.dubbo.api.UserService;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Component;

/**
 * @author huangyongwen
 * @date 2021/11/17
 * @Description
 */
@DubboService
@Component
@Slf4j
public class UserServiceImpl implements UserService {


    @Override
    public User getUserInfo(long userId) {

        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("response from provider: {}" , RpcContext.getContext().getLocalAddress());
        return new User(userId, "userName+" + userId + "  response from provider:" + RpcContext.getContext().getLocalAddress());
    }
}

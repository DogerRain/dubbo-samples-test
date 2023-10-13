package com.dubbo.service;

import com.dubbo.api.UserService;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://learnjava.baimuxym.cn/
 * @site
 * @date 2021/11/17
 * @Description
 */
@Slf4j
@Service("UserServiceImpl2")
public class UserServiceImpl2 implements UserService {

    /**
     *
     * @param userId
     * @return
     * 本地Service实现
     */
    @Override
    public User getUserInfo(long userId) {
        log.debug("我是service下的debug");
        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("response from provider: {}" , RpcContext.getContext().getLocalAddress());
        return new User(userId, "userName" + userId , " --->>>>response from remote RPC provider:" + RpcContext.getContext().getLocalAddress());
    }


}

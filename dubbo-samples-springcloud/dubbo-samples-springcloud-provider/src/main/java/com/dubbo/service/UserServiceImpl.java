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
//这里可以配置 version、group、协议、负载均衡、超时、超时 等等。见xml项目
@DubboService(version = "1.0.0")
@Component
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     *
     * @param userId
     * @return
     * RPC provider 接口 实现
     */
    @Override
    public User getUserInfo(long userId) {
        log.debug("我是service下的debug");
        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("response from provider: {}" , RpcContext.getContext().getLocalAddress());
        return new User(userId, "userName" + userId , " --->>>>response from remote RPC provider:" + RpcContext.getContext().getLocalAddress());
    }

    /**
     *
     * @param userId
     * @return
     * 本地 provider 接口 实现
     */
    public User getUserInfoFromLocal(Long userId){
        return new User(userId,"userName"+userId," --->>>>from Local provider ");
    }

}

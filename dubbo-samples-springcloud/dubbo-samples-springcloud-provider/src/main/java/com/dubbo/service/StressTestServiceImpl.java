package com.dubbo.service;

import com.dubbo.api.StressTestService;
import com.dubbo.api.UserService;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author huangyongwen
 * @date 2021/11/17
 * @Description
 */
//这里可以配置 version、group、协议、负载均衡、超时、超时 等等。见xml项目
@DubboService(version = "1.0.0")
@Component
@Slf4j
public class StressTestServiceImpl implements StressTestService {


    @Override
    public String StressString(String string) {
        log.info("StressString");
        return string;
    }

    @Override
    public User StressTestPojo(User user) {
        log.info("StressTestPojo");
        return user;
    }

    @Override
    public List<User> StressListUser(List<User> list) {
        log.info("StressListUser");
        return list;
    }

    @Override
    public String StressTest50K(String bytes) {
        log.info("StressTest50K");
        return bytes;
    }

    @Override
    public String StressTest100K(String bytes) {
        log.info("StressTest100K");
        return bytes;
    }
}

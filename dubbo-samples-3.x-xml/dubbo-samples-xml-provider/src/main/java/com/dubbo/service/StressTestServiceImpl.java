package com.dubbo.service;

import com.dubbo.api.StressTestService;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://learnjava.baimuxym.cn/
 * @site
 * @date 2021/11/17
 * @Description
 */
@Service("stressTestServiceImpl")
@Slf4j
public class StressTestServiceImpl implements StressTestService {


    @Override
    public String StressString(String string) {
        log.info("StressString");
        doWhileAdd();
        return string;
    }

    @Override
    public String StressString1kNoFile(String string) {
        log.info("StressString1kNoFile");
        doWhileAdd();
        return string;
    }

    @Override
    public String StressString1k(String string) {
        log.info("StressString1k");
        doWhileAdd();
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
        doWhileAdd();
        return bytes;
    }

    void doWhileAdd(){
        Random random = new Random(1);
        int result =0;
        int result2=0;
        for (int i = 1;i<=10000;i++){
            int num = random.nextInt(1000);
            result +=num;
            result2 -=num;
        }
    }

}

package com.dubbo.controller;

import com.dubbo.api.StressTestService;
import com.dubbo.api.UserService;
import com.dubbo.common.FileCapacity;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@RestController
@RequestMapping("/consumer")
@Slf4j
public class StressTestController {



    @DubboReference(version = "*", protocol = "dubbo", loadbalance = "random",timeout = 3000,retries = 0)
    private StressTestService stressTestService;

    private int a = 1;

    @RequestMapping("/stressTest/string")
    public Boolean string(){
        String s = new FileCapacity().getFileCapacity(1*1024);
        String result = stressTestService.StressString(s);
        log.info("stressTest/string:{}",a);
        a++;
        return true;

    }

    @RequestMapping("/stressTest/pojo")
    public Boolean pojo(){
        User user = stressTestService.StressTestPojo(new User(1L,"Stress","压力测试"));
        return true;
    }

    @RequestMapping("/stressTest/listPojo")
    public Boolean listPojo(){
        List<User> list = new ArrayList<>();
        for (int i = 1 ;i<=10;i++){
            User user  = new User((long)i,"userName"+i,"压力测试，listPojo");
            list.add(user);
        }
        List<User> result = stressTestService.StressListUser(list);
        return true;
    }

    @RequestMapping("/stressTest/string50k")
    public Boolean string50k(){
        String s = new FileCapacity().getFileCapacity(50*1024);
        String resuult = stressTestService.StressTest50K(s);
        return true;
    }

    @RequestMapping("/stressTest/string100k")
    public Boolean string100k(){
        String s = new FileCapacity().getFileCapacity(100*1024);
        String resuult = stressTestService.StressTest100K(s);
        return true;
    }
}

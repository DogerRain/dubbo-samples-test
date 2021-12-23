package com.dubbo.controller;

import com.dubbo.api.StressTestService;
import com.dubbo.common.FileCapacity;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/24
 * @Description
 */
@RestController
@RequestMapping("/consumer")
@Slf4j
@Component("stressTestController")
public class StressTestController {

    @DubboReference(version = "*", protocol = "dubbo,hessian", loadbalance = "random",retries = 0)
    private StressTestService stressTestService;

    private int a = 1;


    @RequestMapping("/stressTest/string1kNoFile")
    public Boolean string1kNoFile(){
        String s = "2021-11-2910:32:25.362INFO11552---[io-8091-exec-61]c.dubbo.controller.StressTestController:stressTest/string:430469\n" +
                "2021-11-2910:32:25.362INFO11552---[io-8091-exec-68]c.dubbo.controller.StressTestController:stressTest/string:430470\n" +
                "2021-11-2910:32:25.363INFO11552---[o-8091-exec-102]c.dubbo.controller.StressTestController:stressTest/string:430471\n" +
                "2021-11-2910:32:25.363INFO11552---[io-8091-exec-32]c.dubbo.controller.StressTestController:stressTest/string:430472\n" +
                "2021-11-2910:32:25.365INFO11552---[o-8091-exec-108]c.dubbo.controller.StressTestController:stressTest/string:430473\n" +
                "2021-11-2910:32:25.365INFO11552---[o-8091-exec-110]c.dubbo.controller.StressTestController:stressTest/string:430473\n" +
                "2021-11-2910:32:25.365INFO11552---[o-8091-exec-105]c.dubbo.controller.StressTestController:stressTest/string:430475\n" +
                "2021-11-2910:32:25.366INFO11552---[io-8091-exec-75]c.dubbo.controller.StressTestController:stressTest/string:430476\n" +
                "2021-11-2910:32:25.367INFO11552---[io-8091-exec-81]c.dubbo.controller.StressTestController:st";
        String result = stressTestService.StressString1kNoFile(s);
        log.info("stressTest/string1kNoFile:{},num:{}",result.length(),a);
//        a++;
        return true;

    }

    @RequestMapping("/stressTest/string")
    public Boolean string(){
        String s = "Hello";
        String result = stressTestService.StressString(s);
        log.info("stressTest/string:{},num:{}",result.length(),a);
        return true;

    }

    @RequestMapping("/stressTest/string1k")
    public Boolean string1k(){
        // IO操作读取1k数据
        String s = new FileCapacity().getFileCapacity(1*1024);
        String result = stressTestService.StressString1k(s);
        log.info("stressTest/string1k:{},num:{}",result.length(),a);
        return true;
    }

    @RequestMapping("/stressTest/pojo")
    public Boolean pojo(){
        User user = stressTestService.StressTestPojo(new User(1L,"Stress","压力测试"));
        log.info("/stressTest/pojo:{}",user);
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
        log.info("/stressTest/listPojo:{}",result);
        return true;
    }

    @RequestMapping("/stressTest/string50k")
    public Boolean string50k(){
        String s = new FileCapacity().getFileCapacity(50*1024);
        String result = stressTestService.StressTest50K(s);
        log.info("/stressTest/string50k:{}",result.length());
        return true;
    }

    @RequestMapping("/stressTest/string100k")
    public Boolean string100k(){
        String s = new FileCapacity().getFileCapacity(100*1024);
        String result = stressTestService.StressTest100K(s);
        log.info("/stressTest/string100k:{}",result.length());
        return true;
    }
}

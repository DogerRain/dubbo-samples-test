package com.dubbo.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@RestController
@RequestMapping("/consumer/stress/")
public class StressTestController {

    @DubboReference


    @RequestMapping("/stressTest/string")
    public Boolean string(){
        return true;
    }

    @RequestMapping("/stressTest/pojo")
    public Boolean pojo(){
        return true;
    }

    @RequestMapping("/stressTest/listPojo")
    public Boolean listPojo(){
        return true;
    }

    @RequestMapping("/stressTest/string50k")
    public Boolean string50k(){
        return true;
    }

    @RequestMapping("/stressTest/string100k")
    public Boolean string100k(){
        return true;
    }
}

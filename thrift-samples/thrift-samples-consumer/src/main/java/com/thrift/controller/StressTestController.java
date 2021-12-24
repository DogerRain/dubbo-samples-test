package com.thrift.controller;

import com.thrift.common.FileCapacity;
import com.thrift.config.ThriftConsumerConfiguration;
import com.thrift.service.UserServiceThriftClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/24
 * @Description
 */
@RestController
@RequestMapping("/consumer/stress")
@Slf4j
@Component("stressTestController")
public class StressTestController {

    @Autowired
    ThriftConsumerConfiguration thriftConsumerConfiguration;
    @Autowired
    UserServiceThriftClientImpl userServiceThriftClientImpl;

    @RequestMapping("/string1k")
    public Boolean string1k() {

//        log.info("thrift --->>> controller--->>> string1k");
        FileCapacity fileCapacity = new FileCapacity();
        String s = fileCapacity.getFileCapacity(1 * 1024);
//        最简单的方式
//        String resp = thriftConsumerConfiguration.getRemoteResult(s);
//        String resp = thriftConsumerConfiguration.getRemoteResultTFramedTransport(s);

        String resp = userServiceThriftClientImpl.sendString(s);


        if (resp == null) {
            log.info("string1k:{}", "null");
            return false;
        }
        log.info("string1k:{}", resp.length());
        return true;
    }


    @RequestMapping("/string100k")
    public Boolean string50k() {
//        log.info("thrift --->>> controller--->>> string1k");
        FileCapacity fileCapacity = new FileCapacity();
        String s = fileCapacity.getFileCapacity(100 * 1024);
        String resp = thriftConsumerConfiguration.getRemoteResultTFramedTransport(s);
        if (resp == null) {
            log.info("string100k:{}", "null");
            return false;
        }
        log.info("string100k:{}", resp.length());
        return true;
    }

//    @RequestMapping("/getUser")
//    public Boolean getUser(){
//        log.info("thrift --->>> controller--->>> getUser");
//         User user = userServiceThriftClientImpl.getUser(5);
//         return true;
//    }

}

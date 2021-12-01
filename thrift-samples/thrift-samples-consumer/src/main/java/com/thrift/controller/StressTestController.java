package com.thrift.controller;

import com.shrift.api.Hello;
import com.thrift.common.FileCapacity;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@RestController
@RequestMapping("/consumer/stress")
@Slf4j
public class StressTestController {

    @Qualifier("client")
    Hello.Client client;

    @RequestMapping("/string1k")
    public Boolean string() throws TException {

        FileCapacity fileCapacity = new FileCapacity();
        String s = fileCapacity.getFileCapacity(1*1024);
        String resp = client.helloString(s);
        log.info("string1k:{}",resp.length());
        return true;
    }
    @RequestMapping("/string100k")
    public Boolean string50k() throws TException {
//        Hello.Client client =  configuration.client();
        FileCapacity fileCapacity = new FileCapacity();
        String s = fileCapacity.getFileCapacity(100*1024);
        String resp = client.helloString(s);
        log.info("string50k:{}",resp.length());
        return true;
    }
}

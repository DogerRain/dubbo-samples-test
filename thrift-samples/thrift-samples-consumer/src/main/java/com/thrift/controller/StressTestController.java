package com.thrift.controller;

import com.shrift.api.Hello;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@RestController
@RequestMapping("/thrift/stress")
public class StressTestController {

    @Autowired
    Hello.Client client;

    @RequestMapping("/string")
    public Boolean string() throws TException {
//        Hello.Client client =  configuration.client();
        String resp = client.helloString("wertyuiopsdfghjkl;");
        System.out.println(resp);
        return true;
    }
}

package com.thrift.controller;

import com.thrift.api.Hello;
import com.thrift.common.FileCapacity;
import com.thrift.config.ThriftConsumerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@RestController
@RequestMapping("/consumer/stress")
@Slf4j
public class StressTestController {

//    @Qualifier("client")
//    @Resource
//    Hello.Client client;

    @Value("${thrift.server.host}")
    private String host;
    @Value("${thrift.socket.port}")
    private int port;


    @Autowired
    ThriftConsumerConfiguration thriftConsumerConfiguration;

    @RequestMapping("/string1k")
    public Boolean string()  {

        FileCapacity fileCapacity = new FileCapacity();
        String s = fileCapacity.getFileCapacity(1 * 1024);

//        String resp = thriftConsumerConfiguration.getRemoteResult(s);
        String resp = thriftConsumerConfiguration.getRemoteResultTFramedTransport(s);

        if (resp == null) {
            log.info("string1k:{}", "null");
            return false;
        }
        log.info("string1k:{}", resp.length());
        return true;
    }



    @RequestMapping("/string100k")
    public Boolean string50k() throws TException {
//        Hello.Client client =  configuration.client();
        FileCapacity fileCapacity = new FileCapacity();
        String s = fileCapacity.getFileCapacity(100 * 1024);
//        String resp = client.helloString(s);
//        log.info("string100k:{}", resp.length());
        return true;
    }
}

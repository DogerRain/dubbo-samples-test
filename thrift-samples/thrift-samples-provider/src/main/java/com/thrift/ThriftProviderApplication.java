package com.thrift;

import com.shrift.api.Hello;
import com.thrift.impl.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@SpringBootApplication
@Slf4j
public class ThriftProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThriftProviderApplication.class, args);
        System.out.println("thrift service started..........");
    }

}

package com.thrift;

import com.shrift.api.Hello;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@Component
public class Configuration {

    @Bean
    public Hello.Client client() throws TException {
        System.out.println("初始化bean。。。。");
        // 设置调用的服务地址为本地，端口为 7911
        TTransport transport = new TSocket("localhost", 7911);
        transport.open();
        // 设置传输协议为 TBinaryProtocol
        TProtocol protocol = new TBinaryProtocol(transport);
        Hello.Client client = new Hello.Client(protocol);
        return client;
    }

}



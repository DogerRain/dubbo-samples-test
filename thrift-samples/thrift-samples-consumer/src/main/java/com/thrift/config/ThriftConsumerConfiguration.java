package com.thrift.config;

import com.shrift.api.ConfigThrift;
import com.shrift.api.Hello;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@Component("thriftConsumerConfiguration")
@ComponentScan(value = {"com.thrift"})
@PropertySource("classpath:application-dev.properties")
public class ThriftConsumerConfiguration {

    @Value("${thrift.server.host}")
    private String host;
    @Value("${thrift.socket.port}")
    private int port;

    @Bean(name = "client")
    public Hello.Client client() throws TException {
        System.out.println("初始化bean。。。。");
        // 设置调用的服务地址为本地，端口为 7911
        TTransport transport = new TSocket(host, port);
        // 设置传输协议为 TBinaryProtocol
        TProtocol protocol = new TBinaryProtocol(transport);
        // 协议要和服务端一致
        // TProtocol protocol = new TCompactProtocol(transport);
        // TProtocol protocol = new TJSONProtocol(transport);
        transport.open();
        Hello.Client client = new Hello.Client(protocol);

        return client;
    }

//    @Bean
    public Hello.Client client2() throws TException {
        System.out.println("初始化bean。。。。");
        // 设置调用的服务地址为本地，端口为 7911
        TSocket socket = new TSocket(host, port);
//        socket.setConnectTimeout(5000);  // 设置连接的超时时间
//        socket.setSocketTimeout(2000);  // 设置存取数据的超时时间
        TTransport transport = new TFramedTransport(socket);   // 设置最大的数据帧长度
        transport.open();
        //设置协议
        TProtocol protocol = new TBinaryProtocol(transport);
        Hello.Client client = new Hello.Client(protocol);
        return client;
    }

}



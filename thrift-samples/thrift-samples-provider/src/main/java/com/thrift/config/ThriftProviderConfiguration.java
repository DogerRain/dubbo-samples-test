package com.thrift.config;

import com.shrift.api.Hello;
import com.thrift.impl.HelloServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

/**
 * @author huangyongwen
 * @date 2021/11/30
 * @Description
 */
@Configuration
@ComponentScan(value = {"com.thrift"})
@PropertySource("classpath:application-dev.properties")
public class ThriftProviderConfiguration {

    @Value("${thrift.socket.port}")
    Integer port;

    @Bean
    void setServer(){
        try {
            // 设置服务端口
            TServerSocket serverTransport = new TServerSocket(port);
            // 设置协议工厂为 TBinaryProtocol.Factory
            TBinaryProtocol.Factory proFactory = new TBinaryProtocol.Factory();
            // 关联处理器与 Hello 服务的实现
            TProcessor processor = new Hello.Processor(new HelloServiceImpl());
            TThreadPoolServer.Args args1 = new TThreadPoolServer.Args(serverTransport);
            args1.processor(processor);
            args1.protocolFactory(proFactory);
            //设置服务模型
            TServer server = new TThreadPoolServer(args1);
            System.out.println("Thrift  provider Start on port " + port + "...");
            server.serve();
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

}

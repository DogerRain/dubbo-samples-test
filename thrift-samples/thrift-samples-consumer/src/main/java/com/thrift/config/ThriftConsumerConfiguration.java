package com.thrift.config;

import com.thrift.api.Hello;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.springframework.beans.factory.annotation.Value;
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
@Slf4j
public class ThriftConsumerConfiguration {

    @Value("${thrift.server.host}")
    private String host;
    @Value("${thrift.socket.port}")
    private int port;

    /**
     * TSimpleServer 阻塞模型通道
     *
     * @param s
     * @return
     */
    public String getRemoteResult(String s) {
        try {
            TSocket socket = new TSocket(host, port);
            socket.setTimeout(5000);
            TTransport transport = socket;
            TProtocol protocol = new TCompactProtocol(transport);
            open(transport);
            Hello.Client client = new Hello.Client(protocol);
            String result = client.helloString(s);
            shutdown(transport);
            return result;
        } catch (TTransportException e) {
            log.error("scoket 建立失败", e);
        } catch (TException e) {
            log.error("未知错误", e);
        }

        return null;

    }

    /**
     * 非阻塞服务模型 TFramedTransport通道
     *
     * @param s
     * @return
     */
    public String getRemoteResultTFramedTransport(String s) {
        try {

            TSocket socket = new TSocket(host, port);
            socket.setTimeout(5000);
            TTransport transport = new TFramedTransport(socket);
            TProtocol protocol = new TCompactProtocol(transport);
            open(transport);
            Hello.Client client = new Hello.Client(protocol);
            String result = client.helloString(s);
            shutdown(transport);
            return result;
        } catch (TTransportException e) {
            log.error("scoket 建立失败", e);
        } catch (TException e) {
            log.error("未知错误", e);
        }

        return null;

    }

    private void open(TTransport transport) {
        if (transport != null && !transport.isOpen()) {
            try {
                transport.open();
            } catch (TTransportException e) {
                log.error("transport 打开出错", e);
            }
        }
    }

    private void shutdown(TTransport transport) {
        if (transport != null && transport.isOpen()) {
            try {
                transport.close();
            } catch (Exception e) {
                log.error("transport 关闭出错", e);
            }
        }
    }


}



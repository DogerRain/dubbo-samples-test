package com.thrift; /**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/24
 * @Description
 */

import com.thrift.api.Hello;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.io.Closeable;
import java.io.IOException;

public class HelloServiceClient implements Closeable {
    /**
     * 调用 Hello 服务
     *
     * @param args
     */

    public final TTransport transport;
    public final TProtocol protocol;
    public final Hello.Client client;


    public HelloServiceClient(String host,int port) {
        try {
            transport = new TFramedTransport(new TSocket(host, port));
            protocol = new TCompactProtocol(transport);
            client = new Hello.Client(protocol);
            transport.open();
        } catch (TTransportException e) {
            throw new Error(e);
        }
    }


    @Override
    public void close() throws IOException {
        transport.close();
    }

    public Hello.Client getClient() {
        return client;
    }

    public static void main(String[] args) throws TException {
        HelloServiceClient helloServiceClient = new HelloServiceClient("127.0.0.1",7911);
        String result = helloServiceClient.getClient().helloString("hello");
        System.out.println("result :" + result);
    }

}
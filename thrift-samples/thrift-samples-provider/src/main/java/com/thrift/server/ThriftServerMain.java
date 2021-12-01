package com.thrift.server;

import com.shrift.api.ConfigThrift;
import com.shrift.api.Hello;
import com.thrift.impl.HelloServiceImpl;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.*;
import org.apache.thrift.transport.layered.TFramedTransport;

/**
 * @author huangyongwen
 * @date 2021/11/30
 * @Description
 */
public class ThriftServerMain {
    public static final String SERVER_IP = ConfigThrift.SERVER_HOSTNAME;
    public static final int SERVER_PORT = ConfigThrift.SOCKET_PORT;
    public static final int TIMEOUT = ConfigThrift.TIMEOUT;

    public void startTServer() {
        try {
            /**
             * HelloServiceImpl 实现  Hello 接口
             */
            TProcessor tprocessor = new Hello.Processor<Hello.Iface>(
                    new HelloServiceImpl());
//            Hello.Processor<Hello.Iface> tprocessor =
//             new Hello.Processor<Hello.Iface>(
//             new HelloServiceImpl());

            // 简单的单线程服务模型，一般用于测试
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            TServer.Args tArgs = new TServer.Args(serverTransport);
            tArgs.processor(tprocessor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            //可设置不同协议，但客户端、服务端要一致
            // tArgs.protocolFactory(new TCompactProtocol.Factory());
            // tArgs.protocolFactory(new TJSONProtocol.Factory());
            TServer server = new TSimpleServer(tArgs);
            System.out.println("HelloWorld TSimpleServer start ....");
            server.serve();
        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }

    public void startTNonblockingServer() {
        try {
            /**
             * HelloServiceImpl 实现  Hello 接口
             */
            TProcessor tprocessor = new Hello.Processor<Hello.Iface>(
                    new HelloServiceImpl());
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(
                    SERVER_PORT);
            TNonblockingServer.Args tnbArgs = new TNonblockingServer.Args(
                    tnbSocketTransport);
            tnbArgs.processor(tprocessor);
            tnbArgs.transportFactory(new TFramedTransport.Factory());
            tnbArgs.protocolFactory(new TCompactProtocol.Factory());
            TServer server = new TNonblockingServer(tnbArgs);
            System.out.println("HelloWorld TNonblockingServer start ....");
            server.serve();
        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ThriftServerMain client = new ThriftServerMain();
//        client.startTServer();
//        client.startTNonblockingServer();
    }

}

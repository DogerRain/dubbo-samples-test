package com.thrift;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/24
 * @Description
 */

import com.thrift.api.Hello;
import com.thrift.impl.HelloServiceImpl;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.InetSocketAddress;

public class HelloServiceServer {
    /**
     * 启动 Thrift 服务器
     * @param args
     */
    public static void main(String[] args) throws InterruptedException, TTransportException {
        InetSocketAddress serverAddress = new InetSocketAddress("127.0.0.1", 7911);
        TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);
        TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
        serverParams.protocolFactory(new TCompactProtocol.Factory());
        serverParams.processor( new Hello.Processor<Hello.Iface>(new HelloServiceImpl()));
        TServer server = new TThreadedSelectorServer(serverParams);
        server.serve();
    }
}

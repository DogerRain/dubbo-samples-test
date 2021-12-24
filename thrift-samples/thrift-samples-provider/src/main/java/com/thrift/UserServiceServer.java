package com.thrift;

import com.thrift.api.UserService;
import com.thrift.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.InetSocketAddress;

/**
 * @author huangyongwen
 * @date 2021/12/24
 * @Description
 */
@Slf4j
public class UserServiceServer {

    public static void main(String[] args) throws TTransportException {
        log.info("UserServiceServer启动.....");
        //benchmark-server是hostname
        InetSocketAddress serverAddress = new InetSocketAddress("benchmark-server", 7912);
        TNonblockingServerTransport serverSocket = new TNonblockingServerSocket(serverAddress);
        TThreadedSelectorServer.Args serverParams = new TThreadedSelectorServer.Args(serverSocket);
        serverParams.protocolFactory(new TBinaryProtocol.Factory());
        serverParams.processor(new UserService.Processor<UserService.Iface>(new UserServiceImpl()));
        TServer server = new TThreadedSelectorServer(serverParams);
        server.serve();
    }
}

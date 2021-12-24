package com.thrift;

import com.thrift.api.UserService;
import com.thrift.service.UserServiceThriftClientImpl;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author huangyongwen
 * @date 2021/12/24
 * @Description
 */
public class ThriftUserServiceClient implements Closeable {
    // not thread safe
    public final TTransport transport;
    public final TProtocol protocol;
    public final UserService.Client client;

    public ThriftUserServiceClient(String host, int port) {
        try {
            transport = new TFramedTransport(new TSocket(host, port));
            protocol = new TBinaryProtocol(transport);
            client = new UserService.Client(protocol);
            transport.open();
        } catch (TTransportException e) {
            throw new Error(e);
        }
    }

    @Override
    public void close() throws IOException {
        transport.close();
    }

    public static void main(String[] args) throws IOException {
        try (UserServiceThriftClientImpl userService = new UserServiceThriftClientImpl()) {
            System.out.println(userService.getUser(5));
        }
    }
}

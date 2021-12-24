package com.thrift.service;

import com.thrift.ThriftUserServiceClient;
import com.thrift.api.User;
import com.thrift.pool.LockObjectPool;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;

@Service("userServiceThriftClientImpl")
public class UserServiceThriftClientImpl implements Closeable {
    // 服务提供方地址，如果没有绑定host，请换成ip
    private final String host = "benchmark-server";
    private final int port = 7912;


    /**
     * 使用池子装好32个client
     * 这样子就不会出现 org.apache.thrift.transport.TTransportException: Cannot write to null outputStream、connect timed out
     */
    private final LockObjectPool<ThriftUserServiceClient> clientPool =
            new LockObjectPool<>(32, () -> new ThriftUserServiceClient(host, port));

    @Override
    public void close() throws IOException {
        clientPool.close();
    }


    /**
     * RPC远程调用
     *
     * @param id
     * @return
     */
    public User getUser(long id) {

        ThriftUserServiceClient thriftUserServiceClient = clientPool.borrow();
        try {
            User thriftUser = thriftUserServiceClient.client.getUser(id);
            return thriftUser;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            clientPool.release(thriftUserServiceClient);
        }
    }

    public String sendString(String s) {
        ThriftUserServiceClient thriftUserServiceClient = clientPool.borrow();
        try {
            String re = thriftUserServiceClient.client.sendString(s);
            return re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            clientPool.release(thriftUserServiceClient);
        }
    }


}

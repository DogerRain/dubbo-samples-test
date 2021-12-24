package com.thrift.service;

import com.thrift.HelloServiceClient;
import com.thrift.pool.LockObjectPool;
import org.springframework.stereotype.Service;

import java.io.Closeable;
import java.io.IOException;

@Service("helloServiceThriftClientImpl")
public class HelloServiceThriftClientImpl implements Closeable {

    private final String host = "127.0.0.1";
    private final int port = 7911;


    /**
     * 使用池子装好32个client
     * 这样子就不会出现 org.apache.thrift.transport.TTransportException: Cannot write to null outputStream、connect timed out
     */
    private final LockObjectPool<HelloServiceClient> clientPool =
            new LockObjectPool<>(1, () -> new HelloServiceClient(host,port));

    @Override
    public void close() throws IOException {
        clientPool.close();
    }

    /**
     * RPC远程调用
     * @param s
     * @return
     */
    public String helloString(String s) {

        HelloServiceClient helloServiceClient = clientPool.borrow();
        try {
            String result = helloServiceClient.client.helloString(s);
            return result;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            clientPool.release(helloServiceClient);
        }
    }


}

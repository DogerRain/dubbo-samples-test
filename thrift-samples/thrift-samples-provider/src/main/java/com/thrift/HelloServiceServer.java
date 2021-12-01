package com.thrift;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */

import com.thrift.config.ThriftProviderConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CountDownLatch;

public class HelloServiceServer {
    /**
     * 启动 Thrift 服务器
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ThriftProviderConfiguration.class);
        context.start();
        new CountDownLatch(1).await();
    }
}

package com.thrift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
@SpringBootApplication
public class ThriftProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThriftProviderApplication.class, args);
        System.out.println("dubbo service started..........");
    }

}

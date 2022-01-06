package com.thrift;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/24
 * @Description 借助springboot启动thrift
 */
@SpringBootApplication(scanBasePackages = {"com.thrift"})
@Slf4j
public class ThriftProviderApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ac = SpringApplication.run(ThriftProviderApplication.class, args);
        System.out.println("thrift service started..........");
    }

}

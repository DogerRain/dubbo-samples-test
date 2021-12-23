package com.dubbo.benchmark;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.*;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @date 2021/12/9
 * @Description
 */
@Configuration
@ComponentScan(value = {"com.dubbo.benchmark","com.dubbo.controller"})
@EnableDubbo(scanBasePackages="com.dubbo")
//不支持yml文件扫描
@PropertySource("classpath:/benchmark/application-benchmark.properties")
public class AnnotationConfig {

    @Bean
    @Profile("dev")
    void dev(){
        System.out.println(" i am dev");
    }

    @Bean
    @Profile("test")
    void test(){
        System.out.println(" i am test");
    }

}


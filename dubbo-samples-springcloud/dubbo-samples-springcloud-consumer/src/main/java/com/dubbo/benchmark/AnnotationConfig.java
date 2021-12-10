package com.dubbo.benchmark;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.*;

/**
 * @author huangyongwen
 * @date 2021/12/9
 * @Description
 */
@Configuration
@ComponentScan(value = {"com.dubbo"})
@EnableDubbo(scanBasePackages="com.dubbo")
//不支持yml文件扫描
@PropertySource("classpath:application.properties")
public class AnnotationConfig {

    @Bean
    @Profile("dev")
    void dev(){
        System.out.println(" i am dev");
    }

    @Bean
    void test(){
        System.out.println(" i am test");
    }

}


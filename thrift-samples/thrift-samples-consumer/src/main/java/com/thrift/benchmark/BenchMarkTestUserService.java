package com.thrift.benchmark;

import com.thrift.api.User;
import com.thrift.common.FileCapacity;
import com.thrift.service.UserServiceThriftClientImpl;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/12/20
 * @Description 使用jmh压测 UserService 接口
 */
@State(Scope.Benchmark)
@Slf4j
public class BenchMarkTestUserService {


    public static final int CONCURRENCY = 32;
//    7911 端口
    private final UserServiceThriftClientImpl userService = new UserServiceThriftClientImpl();
//    7912 端口
//    private final HelloServiceThriftClientImpl helloService = new HelloServiceThriftClientImpl();

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public User getUser() {
        log.info("UserService benchmark geUser......");
        return userService.getUser(5);
    }

    @Benchmark
    @BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void sendString() {
        log.info("UserService benchmark sendString......");
        FileCapacity fileCapacity = new FileCapacity();
        String s = fileCapacity.getFileCapacity(1 * 1024);
        userService.sendString(s);
//        helloService.helloString(s);
    }


    @TearDown
    public void close() throws IOException {
        userService.close();
    }


    public static void main(String[] args) throws Exception {
        System.out.println("UserService测试开始");
//        BenchMarkTestUserService client = new BenchMarkTestUserService();
//        for (int i = 0; i < 60; i++) {
//            try {
//                System.out.println(client.getUser());
//                break;
//            } catch (Exception e) {
//                Thread.sleep(1000);
//            }
//        }
//        client.close();

        Options opt = new OptionsBuilder()//
                .include(BenchMarkTestUserService.class.getSimpleName())//
                .warmupIterations(3)//
                .warmupTime(TimeValue.seconds(10))//
                .measurementIterations(3)//
                .measurementTime(TimeValue.seconds(10))//
                .threads(CONCURRENCY)//
                .forks(1)//
                .build();
        new Runner(opt).run();
    }

}

//package com.thrift.benchmark;
//
//import com.thrift.config.ThriftConsumerConfiguration;
//import com.thrift.controller.StressTestController;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.thrift.TException;
//import org.openjdk.jmh.annotations.*;
//import org.openjdk.jmh.results.format.ResultFormatType;
//import org.openjdk.jmh.runner.Runner;
//import org.openjdk.jmh.runner.RunnerException;
//import org.openjdk.jmh.runner.options.Options;
//import org.openjdk.jmh.runner.options.OptionsBuilder;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//import java.io.IOException;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
// * @site
// * @date 2021/12/20
// * @Description
// */
//@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SampleTime})
//@Warmup(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
//@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
//@Threads(32)
//@Fork(1)
//@State(Scope.Benchmark)
//@OutputTimeUnit(TimeUnit.SECONDS)
//@Slf4j
//public class BenchMarkTest {
//
//    private final AnnotationConfigApplicationContext annotationConfigApplicationContext;
//    private final StressTestController stressTestController;
//
//
//    public BenchMarkTest() {
//        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ThriftConsumerConfiguration.class);
//        annotationConfigApplicationContext.start();
//        stressTestController = annotationConfigApplicationContext.getBean("stressTestController", StressTestController.class);
//    }
//
//
//    @TearDown
//    public void close() throws IOException {
//        annotationConfigApplicationContext.close();
//    }
//
//    @Benchmark
//    public void string1k() throws TException {
//
//        log.info("HelloService benchmark string1k......");
//        stressTestController.string1k();
//
//    }
//
//    //    @Benchmark
////    public void string100k() {
////        stressTestController.string100k();
////    }
//
//
//    public static void main(String[] args) throws RunnerException {
//
//        System.out.println("HelloService测试开始");
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        Options opt = new OptionsBuilder()
//                .include(BenchMarkTest.class.getSimpleName())
////                .result("result.json")
//                .resultFormat(ResultFormatType.JSON).build();
//        new Runner(opt).run();
//    }
//
//}

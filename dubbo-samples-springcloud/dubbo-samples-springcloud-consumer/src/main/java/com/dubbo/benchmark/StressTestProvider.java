package com.dubbo.benchmark;

import com.dubbo.controller.StressTestController;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author huangyongwen
 * @date 2021/12/9
 * @Description
 */
@BenchmarkMode({Mode.Throughput, Mode.All})
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
//测量次数,每次测量的持续时间
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Threads(1)
@Fork(1)
@State(value = Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Slf4j
public class StressTestProvider {

    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnnotationConfig.class);
//    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext();

    @Benchmark
    public void string1k() {
//        annotationConfigApplicationContext.register(AnnotationConfig.class);
//        annotationConfigApplicationContext.refresh();
        annotationConfigApplicationContext.start();
        StressTestController annotationAction = annotationConfigApplicationContext.getBean("stressTestController",StressTestController.class);
        annotationAction.string1k();

    }

//    @Benchmark
//    public void string100k() {
//        String s = new FileCapacity().getFileCapacity(100 * 1024);
//        String result = stressTestService.StressTest100K(s);
//        log.info("/stressTest/string100k:{}", result.length());
//    }

    public static void main(String[] args) throws RunnerException {

        log.info("测试开始");
        Options opt = new OptionsBuilder()
                .include(StressTestProvider.class.getSimpleName())
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }


}

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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
    <a>https://rain.baimuxym.cn</a>
 * @date 2021/12/9
 * @Description
 */
@BenchmarkMode({Mode.All})
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
//测量次数,每次测量的持续时间
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
@Threads(32)
@Fork(1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Slf4j
public class StressTestProvider {

    private final AnnotationConfigApplicationContext annotationConfigApplicationContext;
    private final StressTestController stressTestController;

    public StressTestProvider() {
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnnotationConfig.class);
        annotationConfigApplicationContext.start();

        stressTestController = annotationConfigApplicationContext.getBean("stressTestController", StressTestController.class);
    }


    @TearDown
    public void close() throws IOException {
        annotationConfigApplicationContext.close();
    }

    @Benchmark
    public void string1k() {
        stressTestController.string1k();
    }

//    @Benchmark
//    public void string100k() {
//        stressTestController.string100k();
//    }

    public static void main(String[] args) throws RunnerException {

        log.info("测试开始");
        Options opt = new OptionsBuilder()
                .include(StressTestProvider.class.getSimpleName())
//                .warmupIterations(3)
//                .warmupTime(TimeValue.seconds(10))
                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

}

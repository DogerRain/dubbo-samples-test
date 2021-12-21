package com.thrift.benchmark;

import com.thrift.config.ThriftConsumerConfiguration;
import com.thrift.controller.StressTestController;
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
 * @author huangyongwen
 * @date 2021/12/20
 * @Description
 */
@BenchmarkMode({Mode.All})
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
@Threads(2)
@Fork(1)
@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Slf4j
public class BenchMarkTest {

    private final AnnotationConfigApplicationContext annotationConfigApplicationContext;
    private final StressTestController stressTestController;



    public BenchMarkTest() {
        annotationConfigApplicationContext = new AnnotationConfigApplicationContext(ThriftConsumerConfiguration.class);
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
                .include(BenchMarkTest.class.getSimpleName())
//                .result("result.json")
                .resultFormat(ResultFormatType.JSON).build();
        new Runner(opt).run();
    }

}

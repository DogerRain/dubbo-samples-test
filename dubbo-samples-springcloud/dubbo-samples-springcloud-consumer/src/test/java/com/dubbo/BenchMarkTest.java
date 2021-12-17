package com.dubbo;

import com.dubbo.benchmark.AnnotationConfig;
import com.dubbo.controller.StressTestController;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author huangyongwen
 * @date 2021/12/17
 * @Description
 */
@State(Scope.Benchmark)
@BenchmarkMode({Mode.All})
@Warmup(iterations = 3, time = 5, timeUnit = TimeUnit.SECONDS)
//测量次数,每次测量的持续时间
@Measurement(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
@Threads(5)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
public class BenchMarkTest {

    AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(AnnotationConfig.class);

    @Benchmark
    public void string1k() {
        annotationConfigApplicationContext.start();
        StressTestController annotationAction = annotationConfigApplicationContext.getBean("stressTestController",StressTestController.class);
        annotationAction.string1k();
    }

    @Test
    public void doBenchMarkTest() throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchMarkTest.class.getSimpleName())
//                .warmupIterations(5)
//                .measurementIterations(5)
//                .forks(1)
                .build();
        new Runner(opt).run();
    }

}

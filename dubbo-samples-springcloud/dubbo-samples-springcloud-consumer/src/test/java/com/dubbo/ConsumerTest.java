package com.dubbo;

import com.dubbo.benchmark.AnnotationAction;
import com.dubbo.benchmark.AnnotationConfig;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author huangyongwen
 * @date 2021/12/9
 * @Description
 */
public class ConsumerTest {
    @Test
    public void Consumer() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AnnotationConfig.class);
        context.start();
        AnnotationAction annotationAction = context.getBean("annotationAction", AnnotationAction.class);
        annotationAction.StressTest1k();
    }


}

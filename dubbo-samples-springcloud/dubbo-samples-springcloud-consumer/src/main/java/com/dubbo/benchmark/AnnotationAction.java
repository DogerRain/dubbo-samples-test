package com.dubbo.benchmark;

import com.dubbo.api.StressTestService;
import com.dubbo.common.FileCapacity;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @author huangyongwen
 * @date 2021/12/9
 * @Description
 */
@Component("annotationAction")
@Slf4j
public class AnnotationAction {
    @DubboReference(version = "*", protocol = "dubbo,hessian", loadbalance = "random",retries = 0)
    private StressTestService stressTestService;

    public void StressTest1k(){
        String s = new FileCapacity().getFileCapacity(1*1024);
        String result = stressTestService.StressString1k(s);
        log.info("stressTest/string1k:{},num:{}",result.length());
    }

}

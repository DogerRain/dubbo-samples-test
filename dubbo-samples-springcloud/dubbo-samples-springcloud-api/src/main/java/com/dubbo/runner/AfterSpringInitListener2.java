package com.dubbo.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * @Author huangyongwen
 * @Date 2022/6/8 17:51
 * @Description
 **/
@Slf4j
@Service
public class AfterSpringInitListener2 implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //防止重复执行。
        if(contextRefreshedEvent.getApplicationContext().getParent() == null){


            log.info("spring容器启动完成spring容器启动完成2222");


        }

    }
}

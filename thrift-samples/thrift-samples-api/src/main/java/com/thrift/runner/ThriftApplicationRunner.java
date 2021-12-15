package com.thrift.runner;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * 程序启动后通过ApplicationRunner处理一些事务
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/6/6 16:07
 * @since 1.0
 */
@Slf4j
@Component
public class ThriftApplicationRunner extends ContextLoaderListener implements ApplicationRunner {

    Logger logger = LoggerFactory.getLogger(ThriftApplicationRunner.class);

    @Value("${thrift.server.host}")
    private String host;
    @Value("${thrift.socket.port}")
    private int port;


    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void run(ApplicationArguments applicationArguments) {
        log.info("thrift server 启动完成");
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        log.info("我是info日志");
        log.error("我是error日志");
        log.debug("我是debug日志");
        log.info("关键配置信息：");
        String[] activeProfiles = configurableApplicationContext.getEnvironment().getActiveProfiles();
        if (ObjectUtils.isEmpty(activeProfiles)) {
            String[] defaultProfiles = configurableApplicationContext.getEnvironment().getDefaultProfiles();
            log.info("No active profile set, falling back to default profiles: " + StringUtils.arrayToCommaDelimitedString(defaultProfiles));
        } else {
            log.info("The following profiles are active: " + StringUtils.arrayToCommaDelimitedString(activeProfiles));
        }

        log.info("  - thrift.server.host：{}", host);
        log.info("  - thrift.socket.port：{}", port);

    }
}

package com.dubbo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author HaC
 * @date 2023/8/13 23:24
 * @WebSite📶 :  https://learnjava.baimuxym.cn
 * @Description
 */
@Component
@Slf4j
public class CommandLine implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("dubbo service started..........");
    }
}

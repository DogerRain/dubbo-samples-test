/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dubbo;

import com.dubbo.api.UserService;
import com.dubbo.vo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Scanner;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://learnjava.baimuxym.cn/
 */
@Slf4j
public class MainApplication {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext factory = new ClassPathXmlApplicationContext("classpath:dubbo-client-demo.xml");
        factory.start();
        UserService userService = (UserService) factory.getBean("userService");


        while (true){
            Scanner scanner = new Scanner(System.in);
            String next = scanner.next();

            if (next.equals("q")){
                log.info("exit....");
                System.exit(-1);
            }

            User userInfo = userService.getUserInfo(12);
            log.info("result ======>:{}", userInfo);

        }

    }

}

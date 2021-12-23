/*
 *
 *   Licensed to the Apache Software Foundation (ASF) under one or more
 *   contributor license agreements.  See the NOTICE file distributed with
 *   this work for additional information regarding copyright ownership.
 *   The ASF licenses this file to You under the Apache License, Version 2.0
 *   (the "License"); you may not use this file except in compliance with
 *   the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package com.dubbo;

import com.dubbo.api.OrderRESTService;
import com.dubbo.api.OrderRESTService2;
import com.dubbo.api.OrderService;
import com.dubbo.vo.Order;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @date 2021/11/22
 * @Description consumer启动类，rest+dubbo测试 servelt容器 ，这里请使用外部web容器tomcat先启动provider
 */
public class RestConsumer {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/dubbo-consumer-rest.xml"});
        context.start();

        /**
         *  这里不能使用rest协议调用，否则会报 :
         *  You must use at least one, but no more than one http method annotation on: public abstract
         *
         *  因为jax声明@Path注解就是一个http协议，rest又是走的http，所以它会报错有两个 http 方法
         *
         *  还是老老实实把@Path用在接口上吧
         */
        OrderRESTService orderRESTService = context.getBean("orderRESTService", OrderRESTService.class);

        OrderRESTService2 orderRESTService2 = context.getBean("orderRESTService2", OrderRESTService2.class);

        OrderService orderService = context.getBean("orderService", OrderService.class);

        System.out.println("consumer service start ......");
        while (true) {
            System.in.read();
            System.out.println("SUCCESS: got order " + orderRESTService.getOrderInfo(1L));

            String port = "7777";
            getOrder("http://localhost:" + port + "/services/order/2");

//            rest协议可以直接走http
//            getOrder("http://localhost:" + port + "/services/order2/2");

            System.out.println("SUCCESS: got order " + orderRESTService2.getOrderInfo(1L));

            System.out.println("SUCCESS: got order list : " + orderService.getOrderInfo(1L));

        }

    }

    /**
     * 走http调用
     * @param url
     */
    private static void getOrder(String url) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().get();
        try {
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
            }
            System.out.println("SUCCESS: got result: " + response.readEntity(Order.class));
        } finally {
            response.close();
            client.close();
        }
    }

}
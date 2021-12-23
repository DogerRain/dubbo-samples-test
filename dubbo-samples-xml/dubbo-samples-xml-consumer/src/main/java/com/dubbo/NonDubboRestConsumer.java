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


import com.dubbo.vo.Order;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @date 2021/11/22
 * @Description consumer启动类，直接使用http请求rest协议的
 */
public class NonDubboRestConsumer {

    public static void main(String[] args) {
        String port = "7777";

        getUser("http://localhost:" + port + "/services/order2/2");
        getUser("http://localhost:" + port + "/services/order/2");
    }


    private static void getUser(String url) {
        System.out.println("Getting user via " + url);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().get();
        try {
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
            }
            System.out.println("Successfully got result: " + response.readEntity(Order.class));
        } finally {
            response.close();
            client.close();
        }
    }
}

package com.dubbo;

import com.dubbo.api.OrderRESTService2;
import com.dubbo.api.OrderService;
import com.dubbo.vo.Order;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @date 2021/11/22
 * @Description consumer启动类
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/dubbo-consumer.xml"});
        context.start();
        System.out.println("consumer start.....");
//        dubbo
        OrderService orderService1 = context.getBean("orderServiceImpl", OrderService.class);
//        hessian
        OrderService orderService2 = context.getBean("orderServiceImpl2", OrderService.class);
//        rest
        OrderRESTService2 orderRESTService2 = context.getBean("orderRESTService2", OrderRESTService2.class);

        while (true) {
            System.in.read();
            RpcContext rpcContext = RpcContext.getContext();
            System.out.println("OrderService接口第一个实现类");
            System.out.println("SUCCESS: got order list " + orderService1.getOrderInfo(1L));

            System.out.println("SUCCESS: got order list" + orderService2.getOrderInfo(1L));

            System.out.println("SUCCESS: got order " + orderRESTService2.getOrderInfo(1L));

//            rest
            String port = "7777";

            getOrder("http://localhost:" + port + "/services/order/2");
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

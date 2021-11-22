package com.dubbo;

import com.dubbo.api.OrderRESTService;
import com.dubbo.api.OrderService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author huangyongwen
 * @date 2021/11/22
 * @Description
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/dubbo-consumer.xml"});
        context.start();

        OrderService orderService = context.getBean("orderService",OrderService.class);

        while (true) {
            System.in.read();
            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.setAttachment("clientName", "demo");
            rpcContext.setAttachment("clientImpl", "dubbo");
            System.out.println("SUCCESS: got order " + orderService.getOrderInfo(1L));
        }

    }
}

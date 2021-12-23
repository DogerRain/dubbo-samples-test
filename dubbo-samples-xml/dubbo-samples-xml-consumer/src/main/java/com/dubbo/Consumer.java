package com.dubbo;

import com.dubbo.api.OrderService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
//        https://rain.baimuxym.cn
//        dubbo
        OrderService orderService1 = context.getBean("orderServiceImpl", OrderService.class);
//        hessian
//        OrderService orderService2 = context.getBean("orderServiceImpl2", OrderService.class);
//        rest
//        OrderRESTService orderService3 = context.getBean("orderRESTService2", OrderRESTService.class);

        while (true) {
            System.in.read();
            RpcContext rpcContext = RpcContext.getContext();
            System.out.println("SUCCESS: got order list " + orderService1.getOrderInfo(1L));

//            System.out.println("SUCCESS: got order list" + orderService2.getOrderInfo(1L));

//            System.out.println("SUCCESS: got order " + orderService3.getOrderInfo(1L));
        }

    }
}

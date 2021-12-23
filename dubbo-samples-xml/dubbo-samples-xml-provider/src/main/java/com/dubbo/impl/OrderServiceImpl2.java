package com.dubbo.impl;

import com.dubbo.api.OrderService;
import com.dubbo.vo.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/17
 * @Description
 */
@Service("orderServiceImpl2")
@Slf4j
public class OrderServiceImpl2 implements OrderService {

    @Override
    public List<Order> getOrderInfo(long orderId) {
        System.out.println((String.format("request from consumer: {%s}", RpcContext.getContext().getRemoteAddress())));
        System.out.println((String.format("protocol:{%s}", RpcContext.getContext().getProtocol())));
        System.out.println((String.format("response from provider: {%s}", RpcContext.getContext().getLocalAddress())));

        log.info("OrderServiceImpl2方法");
        List<Order> list = new ArrayList<>();
        for (int i = 10; i <= 20; i++) {
            Order order = new Order();
            order.setOrderId((long) i);
            order.setOrderName("MacBook Pro " + i);
            list.add(order);
        }
        return list;
    }
}

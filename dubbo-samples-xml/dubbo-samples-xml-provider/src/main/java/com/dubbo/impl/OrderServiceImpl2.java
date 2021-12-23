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
 * @Description 这是个复杂大对象，用于测试传输大包
 */
@Service("orderServiceImpl2")
@Slf4j
public class OrderServiceImpl2 implements OrderService {

    @Override
    public List<Order> getOrderInfo(long orderId) {

        log.info("OrderServiceImpl2方法");
        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("protocol: {}",RpcContext.getContext().getProtocol());
        log.info("response from provider: {}",RpcContext.getContext().getLocalAddress());
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

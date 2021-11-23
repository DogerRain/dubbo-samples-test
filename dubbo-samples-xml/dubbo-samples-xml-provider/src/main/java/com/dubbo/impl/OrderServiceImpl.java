package com.dubbo.impl;

import com.dubbo.api.OrderService;
import com.dubbo.vo.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author huangyongwen
 * @date 2021/11/17
 * @Description
 */
@Service("orderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public List<Order> getOrderInfo(long orderId) {
        System.out.println((String.format("request from consumer: {%s}", RpcContext.getContext().getRemoteAddress())));
        System.out.println((String.format("protocol:{%s}", RpcContext.getContext().getProtocol())));
        System.out.println((String.format("response from provider: {%s}", RpcContext.getContext().getLocalAddress())));

        List<Order> list = new ArrayList<>();
        Order order1 = new Order();
        order1.setOrderId(199L);
        order1.setOrderName("MacBook Pro 13");

        Order order2 = new Order();
        order2.setOrderId(200L);
        order2.setOrderName("RTX 2060");

        list.add(order1);
        list.add(order2);

        return list;
    }
}

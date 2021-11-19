package com.dubbo.api;

import com.dubbo.vo.Order;

import java.util.List;

public interface OrderService {
   List<Order> getOrderInfo(long orderId);
}

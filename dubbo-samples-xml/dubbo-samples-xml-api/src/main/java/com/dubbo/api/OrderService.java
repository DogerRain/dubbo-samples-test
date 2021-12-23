package com.dubbo.api;

import com.dubbo.vo.Order;

import java.util.List;

/**
 * 该接口有两个实现类
 */
public interface OrderService {
   List<Order> getOrderInfo(long orderId);
}

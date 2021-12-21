package com.dubbo.impl;

import com.dubbo.api.OrderRESTService2;
import com.dubbo.vo.Order;

/**
 * @author huangyongwen
 * @date 2021/12/21
 * @Description
 */
public class OrderRESTServiceImpl2 implements OrderRESTService2 {
    @Override
    public Order getOrderInfo(Long id) {
        return new Order(id, "MacBook Pro");
    }
}

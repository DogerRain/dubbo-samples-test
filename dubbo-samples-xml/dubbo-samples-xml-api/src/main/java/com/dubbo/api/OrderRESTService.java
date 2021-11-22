package com.dubbo.api;

import com.dubbo.vo.Order;


public interface OrderRESTService {

    Order getOrderInfo(Long id);
}

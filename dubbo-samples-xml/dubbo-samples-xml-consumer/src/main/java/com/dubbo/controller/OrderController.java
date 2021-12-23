package com.dubbo.controller;

import com.dubbo.api.OrderService;
import com.dubbo.vo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/22
 * @Description
 */
@RestController
@RequestMapping("/consumer")
public class OrderController {

    @Autowired
    OrderService orderService;

    @RequestMapping("/order/{orderId}")
    public List<Order> getOrderInfo(@PathVariable("orderId") Long orderId) {
        return orderService.getOrderInfo(orderId);
    }
}

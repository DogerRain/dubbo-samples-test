package com.dubbo.impl.rest;

import com.dubbo.api.OrderRESTService2;
import com.dubbo.vo.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.stereotype.Service;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/12/21
 * @Description OrderRESTService2 接口使用 JAX-RS  注解
 */
@Service("orderRESTServiceImpl2")
@Slf4j
public class OrderRESTServiceImpl2 implements OrderRESTService2 {
    @Override
    public Order getOrderInfo(Long id) {
//        System.out.println((String.format("request from consumer: {%s}", RpcContext.getContext().getRemoteAddress())));
//        System.out.println((String.format("protocol:{%s}", RpcContext.getContext().getProtocol())));
//        System.out.println((String.format("response from provider: {%s}", RpcContext.getContext().getLocalAddress())));
        log.info("这是在接口上声明的rest");
        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("protocol: {}",RpcContext.getContext().getProtocol());
        log.info("response from provider: {}",RpcContext.getContext().getLocalAddress());
        return new Order(id, "MacBook Pro");
    }
}

package com.dubbo.impl;

import com.dubbo.api.OrderRESTService;
import com.dubbo.api.OrderService;
import com.dubbo.vo.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangyongwen
 * @date 2021/11/17
 * @Description
 */
@Service("orderRESTServiceImpl")
@Path("provider")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Slf4j
public class OrderRESTServiceImpl implements OrderRESTService {


    @Override
    @GET
    @Path("{id : \\d+}")
    public Order getOrderInfo(@PathParam("id") Long id) {
        System.out.println((String.format("request from consumer: {%s}", RpcContext.getContext().getRemoteAddress())));
        System.out.println((String.format("protocol:{%s}", RpcContext.getContext().getProtocol())));
        System.out.println((String.format("response from provider: {%s}", RpcContext.getContext().getLocalAddress())));
        return new Order(id, "orderName" + id);
    }
}

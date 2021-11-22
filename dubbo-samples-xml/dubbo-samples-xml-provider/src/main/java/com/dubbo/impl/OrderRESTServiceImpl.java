package com.dubbo.impl;

import com.dubbo.api.OrderRESTService;
import com.dubbo.api.OrderService;
import com.dubbo.vo.Order;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huangyongwen
 * @date 2021/11/17
 * @Description
 */
@Path("consumer")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class OrderRESTServiceImpl implements OrderRESTService {


    @Override
    @GET
    @Path("{id : \\d+}")
    public Order getOrderInfo(@PathParam("id") Long id) {
        return new Order(id, "orderName" + id);
    }
}

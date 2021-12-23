package com.dubbo.api;

import com.dubbo.vo.Order;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Path("order2")
public interface OrderRESTService2 {

    @GET
    @Path("{id : \\d+}")
    Order getOrderInfo(@PathParam("id") Long id);
}

package com.dubbo.impl.rest;

import com.dubbo.api.OrderRESTService;
import com.dubbo.vo.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.protocol.rest.support.ContentType;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
  * @site
 * @date 2021/11/17
 * @Description  这里在实现类加上 JAX-RS 的注解，表示提供REST服务，类似于 springMVC 的 @RestController、@RequestMapping
 */
@Service("orderRESTServiceImpl")
@Path("order")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Slf4j
public class OrderRESTServiceImpl implements OrderRESTService {


    @Override
    @GET
    @Path("{id : \\d+}")
    public Order getOrderInfo(@PathParam("id") Long id /*@Context HttpServletRequest request 这种方法也可以获取到上下文*/) {
//        System.out.println((String.format("request from consumer: {%s}", RpcContext.getContext().getRemoteAddress())));
//        System.out.println((String.format("protocol:{%s}", RpcContext.getContext().getProtocol())));
//        System.out.println((String.format("response from provider: {%s}", RpcContext.getContext().getLocalAddress())));
        log.info("这是在实现类上声明的rest");
        log.info("request from consumer: {}",RpcContext.getContext().getRemoteAddress());
        log.info("protocol: {}",RpcContext.getContext().getProtocol());
        log.info("response from provider: {}",RpcContext.getContext().getLocalAddress());
        return new Order(id, "MacBook Air" + id);
    }
}

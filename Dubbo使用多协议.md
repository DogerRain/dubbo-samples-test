Dubbo是支持多种协议的，这里我会 演示 dubbo（默认）、hessian、rest 这三种协议。

假如我有这样一个场景：

OrderService 接口有两个实现类，其中一个 OrderServiceImpl 获取的数据较小，我想通过dubbo协议调用；而另外一个 OrderServiceImpl2 获取的数据较大，我想通过 hessian协议调用。

或者我想直接通过Http调用provider的接口。





```java
2021-12-23 09:58.21 [http-nio-7777-exec-1] -- [ INFO] - rest.OrderRESTServiceImpl: 这是在实现类上声明的rest
2021-12-23 09:58.21 [http-nio-7777-exec-1] -- [ INFO] - rest.OrderRESTServiceImpl: request from consumer: 127.0.0.1:40480
2021-12-23 09:58.21 [http-nio-7777-exec-1] -- [ INFO] - rest.OrderRESTServiceImpl: protocol: dubbo
2021-12-23 09:58.21 [http-nio-7777-exec-1] -- [ INFO] - rest.OrderRESTServiceImpl: response from provider: 172.16.44.48:7777
    
2021-12-23 09:58.21 [http-nio-7777-exec-3] -- [ INFO] - rest.OrderRESTServiceImpl2: 这是在接口上声明的rest
2021-12-23 09:58.21 [http-nio-7777-exec-3] -- [ INFO] - rest.OrderRESTServiceImpl2: request from consumer: 172.16.44.48:40481
2021-12-23 09:58.21 [http-nio-7777-exec-3] -- [ INFO] - rest.OrderRESTServiceImpl2: protocol: dubbo
2021-12-23 09:58.21 [http-nio-7777-exec-3] -- [ INFO] - rest.OrderRESTServiceImpl2: response from provider: 172.16.44.48:7777
    
2021-12-23 09:58.22 [DubboServerHandler-172.16.44.48:20880-thread-2] -- [ INFO] - impl.OrderServiceImpl: OrderServiceImpl方法
2021-12-23 09:58.22 [DubboServerHandler-172.16.44.48:20880-thread-2] -- [ INFO] - impl.OrderServiceImpl: request from consumer: /172.16.44.48:40478
2021-12-23 09:58.22 [DubboServerHandler-172.16.44.48:20880-thread-2] -- [ INFO] - impl.OrderServiceImpl: protocol: null
2021-12-23 09:58.22 [DubboServerHandler-172.16.44.48:20880-thread-2] -- [ INFO] - impl.OrderServiceImpl: response from provider: 172.16.44.48:20880
```



踩坑：

使用 rest 协议的时候，启动consumer 发现报错：

```
You must use at least one, but no more than one http method annotation on: public abstract
```

原因是 **实现类**使用 `@Path`、`@Consumes`、`@Produces` 注解：

```java
@Service("orderRESTServiceImpl")
@Path("provider")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class OrderRESTServiceImpl implements OrderRESTService {
```

consumer.xml 又配置了：

```xml
<dubbo:reference id="orderRESTServiceImpl" 
interface="com.dubbo.api.OrderRESTService" protocol="rest"/>
```

使用了JAX-RS 的注解，本来就提供REST服务，所以就会出现两个REST，提示报错。

解决方法：

- 使用JAX-RS的注解只注解到 接口
- 使用http直连的方法访问即可，rest协议可以直接使用 http 访问
Dubbo是支持多种协议的，这里我会 演示 dubbo（默认）、hessian、rest 这三种协议。文章代码贴的比较多，代码已经上传到GitHub，见文末。

假如我有这样一个场景：

OrderService 接口有两个实现类，其中一个 OrderServiceImpl 获取的数据较小，我想通过dubbo协议调用；而另外一个 OrderServiceImpl2 获取的数据较大，我想通过 hessian协议调用，或者我想直接通过Http调用provider的接口。

这要如何配置呢？

下面来研究一下这几种协议的使用。

> TODO：可以使用jmh压测不同协议传输不同的数据量，进行性能对比

项目结构：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211122140325912.png)

父类pom，引入协议需要的依赖：

```xml
    <modules>
        <module>dubbo-samples-xml-api</module>
        <module>dubbo-samples-xml-provider</module>
        <module>dubbo-samples-xml-consumer</module>
    </modules>

    <description>Demo project for Spring</description>
    <properties>
        <source.level>1.8</source.level>
        <target.level>1.8</target.level>
        <dubbo.version>2.7.13</dubbo.version>
        <!--<dubbo.version>3.0.2.1</dubbo.version>-->
        <spring.version>4.3.16.RELEASE</spring.version>
        <junit.version>4.12</junit.version>
        <tomcat.version>8.0.53</tomcat.version>
        <validation-api.version>1.1.0.Final</validation-api.version>
        <hibernate-validator.version>4.2.0.Final</hibernate-validator.version>
    </properties>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-framework-bom</artifactId>
                <version>${spring.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.dubbo</groupId>
                <artifactId>dubbo-bom</artifactId>
                <version>${dubbo.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <dependency>
                <groupId>org.apache.dubbo</groupId>
                <artifactId>dubbo-dependencies-zookeeper</artifactId>
                <version>${dubbo.version}</version>
                <type>pom</type>
            </dependency>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
                <scope>test</scope>
            </dependency>
            <dependency>
                <groupId>javax.validation</groupId>
                <artifactId>validation-api</artifactId>
                <version>${validation-api.version}</version>
            </dependency>
            <dependency>
                <groupId>org.hibernate</groupId>
                <artifactId>hibernate-validator</artifactId>
                <version>${hibernate-validator.version}</version>
            </dependency>
             <!--Tomcat内嵌包-->
            <dependency>
                <groupId>org.apache.tomcat.embed</groupId>
                <artifactId>tomcat-embed-core</artifactId>
                <version>${tomcat.version}</version>
            </dependency>

        </dependencies>
    </dependencyManagement>
```



## 1、新建api项目

新建dubbo-samples-xml-api项目

**pom.xml：**

```xml
  <dependency>
        <groupId>org.apache.dubbo</groupId>
        <artifactId>dubbo-rpc-rest</artifactId>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.12</version>
    </dependency>
```
定义接口：

**OrderRESTService：**

```java
public interface OrderRESTService {
    Order getOrderInfo(Long id);
}
```

**OrderRESTService2，一个标准的 JAX-RS rest服务：**

```java
@Consumes({MediaType.APPLICATION_JSON})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Path("order2")
public interface OrderRESTService2 {
    @GET
    @Path("{id : \\d+}")
    Order getOrderInfo(@PathParam("id") Long id);
}
```

**OrderService：普通接口**

```java
public interface OrderService {
   List<Order> getOrderInfo(long orderId);
}
```

**Order：实体类**

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = -3757842094691885448L;
    Long orderId;
    String orderName;

}
```

## 2、创建provider

创建一个dubbo-samples-xml-provider 项目。

**pom.xml：**

```xml
<dependencies>
        <dependency>
            <groupId>com.dubbo</groupId>
            <artifactId>dubbo-samples-xml-api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
        </dependency>

        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.tomcat.embed</groupId>
            <artifactId>tomcat-embed-core</artifactId>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
        </dependency>
        <!--dubbo支持rest-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-rpc-rest</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-registry-zookeeper</artifactId>
        </dependency>
        <!--dubbo使用hessian-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-rpc-hessian</artifactId>
        </dependency>
    </dependencies>
```

创建四个实现类：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211223154913179.png)

其中 OrderServiceImpl、OrderServiceImpl2 均实现 OrderService 接口，具体代码如下：

**OrderServiceImpl：**

```java
@Service("orderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Override
    public List<Order> getOrderInfo(long orderId) {
        log.info("OrderServiceImpl方法");
        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("protocol: {}",RpcContext.getContext().getProtocol());
        log.info("response from provider: {}",RpcContext.getContext().getLocalAddress());
        List<Order> list = new ArrayList<>();
        Order order1 = new Order();
        order1.setOrderId(199L);
        order1.setOrderName("MacBook Pro 13");

        Order order2 = new Order();
        order2.setOrderId(200L);
        order2.setOrderName("RTX 2060");

        list.add(order1);
        list.add(order2);

        return list;
    }
}
```

**OrderServiceImpl2：**

```java
/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/17
 * @Description 这是个复杂大对象，用于测试传输大包
 */
@Service("orderServiceImpl2")
@Slf4j
public class OrderServiceImpl2 implements OrderService {

    @Override
    public List<Order> getOrderInfo(long orderId) {

        log.info("OrderServiceImpl2方法");
        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("protocol: {}",RpcContext.getContext().getProtocol());
        log.info("response from provider: {}",RpcContext.getContext().getLocalAddress());
        List<Order> list = new ArrayList<>();
        for (int i = 10; i <= 20; i++) {
            Order order = new Order();
            order.setOrderId((long) i);
            order.setOrderName("MacBook Pro " + i);
            list.add(order);
        }
        return list;
    }
}
```

**OrderRESTServiceImpl：**

```java
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
        log.info("这是在实现类上声明的rest");
        log.info("request from consumer: {}",RpcContext.getContext().getRemoteAddress());
        log.info("protocol: {}",RpcContext.getContext().getProtocol());
        log.info("response from provider: {}",RpcContext.getContext().getLocalAddress());
        return new Order(id, "MacBook Air" + id);
    }
}
```

OrderRESTServiceImpl 在实现类上使用 JAX-RS  注解，类似于 spring-mvc 的 @RestController、@RequestMapping

**OrderRESTServiceImpl2：**

```java
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
        log.info("这是在接口上声明的rest");
        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("protocol: {}",RpcContext.getContext().getProtocol());
        log.info("response from provider: {}",RpcContext.getContext().getLocalAddress());
        return new Order(id, "MacBook Pro");
    }
}
```

**创建dubbo-provider.xml，声明dubbo的配置：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <context:property-placeholder/>
    <context:component-scan base-package="com.dubbo.impl"/>
    <dubbo:application name="dubbo-samples-provider-xml"/>

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:${zookeeper.port:2181}"/>

    <dubbo:protocol name="dubbo" port="20883"/>

    <!--hessian需要借助servlet容器，这里使用内部tomcat-->
    <dubbo:protocol name="hessian" port="8888" server="tomcat" />

    <!--rest使用外部tomcat，端口需要和外部tomcat一致-->
    <!--<dubbo:protocol name="rest" port="7777" contextpath="services" server="servlet"/>-->

    <!--rest使用内部tomcat-->
    <dubbo:protocol name="rest" port="7777" threads="500" contextpath="services" server="tomcat" accepts="500"/>

    <!--这个接口仅支持dubbo协议-->
    <dubbo:service interface="com.dubbo.api.OrderService"
                   ref="orderServiceImpl" protocol="dubbo" group="one"/>

    <!--这个接口仅支持hessian协议-->
    <dubbo:service interface="com.dubbo.api.OrderService"
                   ref="orderServiceImpl2" protocol="hessian" group="two"/>

    <!--这个是rest协议，实现类使用 JAX-RS-->
    <dubbo:service interface="com.dubbo.api.OrderRESTService"
                   ref="orderRESTServiceImpl" protocol="rest"/>

    <!--这个是rest协议，接口使用 JAX-RS-->
    <dubbo:service interface="com.dubbo.api.OrderRESTService2"
                   ref="orderRESTServiceImpl2" protocol="rest" />

</beans>
```

这里声明了三种协议，协议注意不同的端口，`server="tomcat"`  表示使用的是内部tomcat。

**Provider启动类：**

```java
public class Provider {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/dubbo-provider.xml"});
        context.start();
        System.out.println("provider service start ......");
        new CountDownLatch(1).await();
    }
}
```

## 3、创建consumer

创建dubbo-samples-xml-consumer项目。

**pom：**

```xml
    <dependencies>
        <dependency>
            <groupId>com.dubbo</groupId>
            <artifactId>dubbo-samples-xml-api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-dependencies-zookeeper</artifactId>
            <type>pom</type>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
        </dependency>
        <!--dubbo支持rest-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-rpc-rest</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-registry-zookeeper</artifactId>
        </dependency>
        <!--hessian-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-rpc-hessian</artifactId>
        </dependency>

    </dependencies>
```

**dubbo-consumer.xml 配置类：**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
       xmlns="http://www.springframework.org/schema/beans"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
       http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd">

    <dubbo:application name="dubbo-samples-consumer-xml"/>

    <dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:${zookeeper.port:2181}"/>

    <dubbo:reference id="orderServiceImpl" check="false"
                     interface="com.dubbo.api.OrderService"  protocol="dubbo" group="one"/>

    <dubbo:reference id="orderServiceImpl2" check="false"
                     interface="com.dubbo.api.OrderService"  protocol="hessian" group="two"/>

    <!--rest，接口使用jax-->
    <dubbo:reference id="orderRESTService2" check="false"
                     interface="com.dubbo.api.OrderRESTService2"  protocol="rest"/>

</beans>
```

**Consumer启动类：**

```java
/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @date 2021/11/22
 * @Description consumer启动类
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/dubbo-consumer.xml"});
        context.start();
        System.out.println("consumer start.....");
//        dubbo
        OrderService orderService1 = context.getBean("orderServiceImpl", OrderService.class);
//        hessian
        OrderService orderService2 = context.getBean("orderServiceImpl2", OrderService.class);
//        rest
        OrderRESTService2 orderRESTService2 = context.getBean("orderRESTService2", OrderRESTService2.class);

        while (true) {
            System.in.read();
            RpcContext rpcContext = RpcContext.getContext();
            System.out.println("SUCCESS: got order list " + orderService1.getOrderInfo(1L));

            System.out.println("SUCCESS: got order list" + orderService2.getOrderInfo(1L));

            System.out.println("SUCCESS: got order " + orderRESTService2.getOrderInfo(1L));

//            rest
            String port = "7777";

            getOrder("http://localhost:" + port + "/services/order/2");
        }

    }
    /**
     * 走http调用
     * @param url
     */
    private static void getOrder(String url) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(url);
        Response response = target.request().get();
        try {
            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed with HTTP error code : " + response.getStatus());
            }
            System.out.println("SUCCESS: got result: " + response.readEntity(Order.class));
        } finally {
            response.close();
            client.close();
        }
    }
}
```

## 4、测试

执行 provider 测试类，打开dubbo-admin 项目（dubbo官方的可视化面板，需要自行搭建），可以看到有四个提供接口：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211223160934298.png)

点击某个接口，可以看到详细的信息：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211223161057421.png)

启动consumer测试类，可以看到日志：

consumer日志：

```java
SUCCESS: got order list [Order(orderId=199, orderName=MacBook Pro 13), Order(orderId=200, orderName=RTX 2060)]
    
SUCCESS: got order list[Order(orderId=10, orderName=MacBook Pro 10), Order(orderId=11, orderName=MacBook Pro 11), Order(orderId=12, orderName=MacBook Pro 12), Order(orderId=13, orderName=MacBook Pro 13), Order(orderId=14, orderName=MacBook Pro 14), Order(orderId=15, orderName=MacBook Pro 15), Order(orderId=16, orderName=MacBook Pro 16), Order(orderId=17, orderName=MacBook Pro 17), Order(orderId=18, orderName=MacBook Pro 18), Order(orderId=19, orderName=MacBook Pro 19), Order(orderId=20, orderName=MacBook Pro 20)]
SUCCESS: got order Order(orderId=1, orderName=MacBook Pro)
    
SUCCESS: got result: Order(orderId=2, orderName=MacBook Air2)
```

provider日志：

```java
	# 这里是dubbo协议调用
2021-12-23 15:29.33 [DubboServerHandler-172.16.44.48:20883-thread-23] -- [ INFO] - impl.OrderServiceImpl: OrderServiceImpl方法
2021-12-23 15:29.33 [DubboServerHandler-172.16.44.48:20883-thread-23] -- [ INFO] - impl.OrderServiceImpl: request from consumer: /172.16.44.48:54311
2021-12-23 15:29.33 [DubboServerHandler-172.16.44.48:20883-thread-23] -- [ INFO] - impl.OrderServiceImpl: protocol: null
2021-12-23 15:29.33 [DubboServerHandler-172.16.44.48:20883-thread-23] -- [ INFO] - impl.OrderServiceImpl: response from provider: 172.16.44.48:20883
    
    # 这里是hessian协议调用
2021-12-23 15:29.33 [http-nio-8888-exec-6] -- [ INFO] - impl.OrderServiceImpl2: OrderServiceImpl2方法
2021-12-23 15:29.33 [http-nio-8888-exec-6] -- [ INFO] - impl.OrderServiceImpl2: request from consumer: 172.16.44.48:54314
2021-12-23 15:29.33 [http-nio-8888-exec-6] -- [ INFO] - impl.OrderServiceImpl2: protocol: dubbo
2021-12-23 15:29.33 [http-nio-8888-exec-6] -- [ INFO] - impl.OrderServiceImpl2: response from provider: 172.16.44.48:8888
    
    # 这里是rest协议调用
2021-12-23 15:29.33 [http-nio-7777-exec-1] -- [ INFO] - rest.OrderRESTServiceImpl2: 这是在接口上声明的rest
2021-12-23 15:29.33 [http-nio-7777-exec-1] -- [ INFO] - rest.OrderRESTServiceImpl2: request from consumer: 172.16.44.48:54315
2021-12-23 15:29.33 [http-nio-7777-exec-1] -- [ INFO] - rest.OrderRESTServiceImpl2: protocol: dubbo
2021-12-23 15:29.33 [http-nio-7777-exec-1] -- [ INFO] - rest.OrderRESTServiceImpl2: response from provider: 172.16.44.48:7777
    
    # 这里是http协议调用
2021-12-23 15:29.33 [http-nio-7777-exec-2] -- [ INFO] - rest.OrderRESTServiceImpl: 这是在实现类上声明的rest
2021-12-23 15:29.33 [http-nio-7777-exec-2] -- [ INFO] - rest.OrderRESTServiceImpl: request from consumer: 127.0.0.1:54316
2021-12-23 15:29.33 [http-nio-7777-exec-2] -- [ INFO] - rest.OrderRESTServiceImpl: protocol: dubbo
2021-12-23 15:29.33 [http-nio-7777-exec-2] -- [ INFO] - rest.OrderRESTServiceImpl: response from provider: 172.16.44.48:7777
```

在dubbo-admin可以看到有三个消费者（rest、hessian、dubbo 各一个协议的消费者，第四个是http直连的）：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211223161328708.png)

## 5、其他

### 5.1、rest

dubbo支持rest（dubbo集成了JAX-RS）在使用上，这里和spring有异曲同工之妙，简单使用几个JAX-RS注解就可以使用了，如：

```java
@Path("users")
public class UserServiceImpl implements UserService {

    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    public void registerUser(User user) {
        // save the user...
    }
}
```

- @Path("users")：指定访问UserService的URL相对路径是/users，即http://localhost:8080/users

- @Path("register")：指定访问registerUser()方法的URL相对路径是/register，再结合上一个@Path为UserService指定的路径，则调用UserService.register()的完整路径为http://localhost:8080/users/register

- @POST：指定访问registerUser()用HTTP POST方法

- @Consumes({MediaType.APPLICATION_JSON})：指定registerUser()接收JSON格式的数据。REST框架会自动将JSON数据反序列化为User对象

然后在配置中声明需要暴露的服务接口即可：

```xml
<dubbo:service interface="com.dubbo.api.OrderRESTService2"
	ref="orderRESTServiceImpl2" protocol="rest" />
```



### 5.2、踩坑：

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

- 使用JAX-RS的注解到接口上，而不是实现类
- 使用http直连的方法访问即可，rest协议可以直接使用 http 访问

**那 Annotation放在接口类还是实现类？**

在一般应用中， 建议放在实现类，便于维护，又不用污染接口，万一该接口有多个实现类就....

如果接口和实现类都同时添加了annotation，则实现类的annotation配置会生效，接口上的annotation被直接忽略。

当然你都放在实现类了，在consumer的配置文件，就不要使用 `protocol="rest"` 这种方式调用了，直接使用http连接就可以了。

### 5.3、rest、hessian

这两个协议需要依赖 servlet 容器，当然你也可以使用**外部的servlet容器**，只需要在你的`web.xml`配置：

```xml
<?xml version="1.0"?>
<!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
        "http://java.sun.com/dtd/web-app_2_3.dtd">

<web-app>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:/spring/dubbo-provider-rest.xml</param-value>
    </context-param>

    <!--this listener must be defined before the spring listener-->
    <listener>
        <listener-class>org.apache.dubbo.remoting.http.servlet.BootstrapListener</listener-class>
    </listener>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.apache.dubbo.remoting.http.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/services/*</url-pattern>
    </servlet-mapping>
</web-app>
```

在配置文件中声明：

```xml
<!--使用外部tomcat，端口需要和外部tomcat一致-->
    <dubbo:protocol name="rest" port="7777" contextpath="services" server="servlet"/>
```

`contextpath` 必须要和 web.xml 配置的 `url-pattern` 一致

端口也必须要和外部servlet容器一致

未来TODO：

- [ ] 使用ab压测dubbo不同协议的性能（dubbo协议已压，见其他系列文章）
- [ ] dubbo3.0新版本triple协议的使用

---

参考：

- 使用 rest 协议：https://dangdangdotcom.github.io/dubbox/rest.html
# Dubbo整合Spring（xml形式）

Dubbo 在 spring xml 形式的配置要比SpringBoot 注解形式的配置 可读性要更友好。

> springboot项目也推荐使用 xml 形式配置 dubbo

因为Dubbo 是可以具体到方法级别的，使用xml配置起来更直观。（官方也推荐）

本文就来使用 Dubbo 2.7  整合 Spring。

项目结构如下：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211122140325912.png)



父类项目依赖 `pom.xml` ：

```xml
	<properties>
        <dubbo.version>2.7.13</dubbo.version>
        <spring.version>4.3.16.RELEASE</spring.version>
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

        </dependencies>
    </dependencyManagement>
```



## 1、创建api

创建`dubbo-samples-xml-api`项目，定义一个接口：

OrderService.java

```java
public interface OrderService {
   List<Order> getOrderInfo(long orderId);
}
```

Order.java：

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

dubbo-provider.xml：

```xml
<context:component-scan base-package="com.dubbo.impl"/>
<dubbo:application name="dubbo-samples-provider-xml"/>
<dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:${zookeeper.port:2181}"/>

<dubbo:protocol name="dubbo" port="20883"/>
<dubbo:service interface="com.dubbo.api.OrderService" ref="orderServiceImpl" protocol="dubbo" group="one"/>
```

OrderServiceImpl.java ，实现接口：

```java
@Service("orderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public List<Order> getOrderInfo(long orderId) {
        
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

启动类Provider：

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

dubbo-consumer.xml：

```xml
<dubbo:application name="dubbo-samples-consumer-xml"/>

<dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:${zookeeper.port:2181}"/>

<dubbo:reference id="orderServiceImpl" check="false"
interface="com.dubbo.api.OrderService" protocol="dubbo" group="one"/>
```

实现类 OrderServiceImpl：

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

启动类Consumer：

```java
/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @date 2021/11/22
 * @Description consumer启动类，按下enter键
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring/dubbo-consumer.xml"});
        context.start();
        System.out.println("consumer start.....");
//        dubbo
        OrderService orderService1 = context.getBean("orderServiceImpl", OrderService.class);
        while (true) {
            System.in.read();
            RpcContext rpcContext = RpcContext.getContext();
            System.out.println("SUCCESS: got order list " + orderService1.getOrderInfo(1L));
        }
    }
}
```



## 4、测试

启动 provider，打开dubbo-admin（需要自行搭建，请参考：），可以看到provider已经注册进来了：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211223105619449.png)

启动consumer，按下enter键。

consumer日志：

```java
SUCCESS: got order list [Order(orderId=199, orderName=MacBook Pro 13), Order(orderId=200, orderName=RTX 2060)]
```

privider日志：

```java
2021-12-23 11:42.35 [DubboServerHandler-172.16.44.48:20883-thread-2] -- [ INFO] - impl.OrderServiceImpl: OrderServiceImpl方法
2021-12-23 11:42.35 [DubboServerHandler-172.16.44.48:20883-thread-2] -- [ INFO] - impl.OrderServiceImpl: request from consumer: /172.16.44.48:47287
2021-12-23 11:42.35 [DubboServerHandler-172.16.44.48:20883-thread-2] -- [ INFO] - impl.OrderServiceImpl: protocol: null
2021-12-23 11:42.35 [DubboServerHandler-172.16.44.48:20883-thread-2] -- [ INFO] - impl.OrderServiceImpl: response from provider: 172.16.44.48:20883
```



## 5、总结

Dubbo是支持多种协议的，以上我演示的是 dubbo（默认）协议。除了该协议，dubbo也支持其他协议，专栏其他文章我做了dubbo、hessian、rest协议的演示：



相对SpringBoot的yml、properties配置，xml的配置更清晰明了，个人也推荐使用xml的方式配置。


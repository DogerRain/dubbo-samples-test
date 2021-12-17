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

dubbo-provider.xml:



OrderServiceImpl.java ，实现接口：

```java
@Service("orderServiceImpl")
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    public List<Order> getOrderInfo(long orderId) {
        System.out.println((String.format("request from consumer: {%s}", RpcContext.getContext().getRemoteAddress())));
        System.out.println((String.format("protocol:{%s}", RpcContext.getContext().getProtocol())));
        System.out.println((String.format("response from provider: {%s}", RpcContext.getContext().getLocalAddress())));

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
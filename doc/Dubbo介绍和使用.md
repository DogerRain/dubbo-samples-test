## 1、介绍

Dubbo官方文档：[https://dubbo.apache.org/zh](https://dubbo.apache.org/zh)

目前Dubbo最新的是Dubbo3。本文使用的是 2.7.3 （推荐）

Dubbo3 格式的 Provider 地址不能被 Dubbo2 的 Consumer 识别到，反之 Dubbo2 的消费者也不能订阅到 Dubbo3 Provider。

>  这里的架构和使用都是基于Dubbo2.7 版本，Dubbo2（2.7以下）和Dubbo3两个版本的注解有区别。



![](https://dubbo.apache.org/imgs/architecture.png)

Registry是注册中心，用于发现服务者和消费者。

注册中心可以选择 zookeeper、consul、nacos，推荐使用zookeeper。

dubboRPC通信的原理：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202202/4202472-9d81b0d43259092d.png)

dubbo和springCloud的区别：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202202/image-20220304170147949.png)

## 2、架构

Dubbo三大组件：

- 注册中心。协调 Consumer 与 Provider 之间的地址注册与发现
- 配置中心。
  - 存储 Dubbo 启动阶段的全局配置，保证配置的跨环境共享与全局一致性
  - 负责服务治理规则（路由规则、动态配置等）的存储与推送。
- 元数据中心。
  - 接收 Provider 上报的服务接口元数据，为 Admin 等控制台提供运维能力（如服务测试、接口文档等）
  - 作为服务发现机制的补充，提供额外的接口/方法级别配置信息的同步能力，相当于注册中心的额外扩展

![](https://dubbo.apache.org/imgs/v3/concepts/threecenters.png)

以上三个中心并不是运行 Dubbo 的必要条件，用户完全可以根据自身业务情况决定只启用其中一个或多个，以达到简化部署的目的。

通常情况下，所有用户都会以独立的注册中心 开始 Dubbo 服务开发，而配置中心、元数据中心则会在微服务演进的过程中逐步的按需被引入进来。



## 3、使用

依赖：（推荐使用2.7.13版本）。

spring集成dubbo：

```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.7.13</version>
</dependency>
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-registry-zookeeper</artifactId>
    <version>2.7.13</version>
</dependency>
```



springboot与dubbo集成：

```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.13</version>
</dependency>
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-registry-zookeeper</artifactId>
    <version>2.7.13</version>
</dependency>
```



### 3.1、spring.xml配置

#### 1、服务端

在服务提供方实现接口(对服务消费方隐藏实现)：

spring-provider.xml ：

```xml
<!--注意：配置集群的情况下需要同一集群的name值相同-->
<dubbo:application name="demo-provider" />
<!-- 使用zookeeper注册中心暴露服务地址 --> 
<dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181" />
<!--实际项目中使用properties文件的形式定义zookeeper的地址 -->
<!-- <dubbo:registry protocol="zookeeper" address="${zookeeper.address}"  check="false" file="dubbo.properties" /> -->


<dubbo:provider token="true" />
<!-- beanId -->
<bean id="demoService" class="org.apache.dubbo.samples.basic.impl.DemoServiceImpl"/>

<!--用 Spring 配置声明暴露服务，ref="demoService" 是 beanId，必填 -->
<dubbo:service interface="org.apache.dubbo.samples.basic.api.DemoService" ref="demoService" />
 <!--使用dubbo协议，声明服务的端口-->
<dubbo:protocol name="dubbo" port="20898"/>

```



一般来说，我们会把 beanId 交给 spring 去管理只需要在 xml 里面声明扫描包，然后使用 `@Service` 声明实现类 即可： 

```xml
<context:component-scan base-package="org.apache.dubbo.samples.basic.impl"/>
```



#### 2、客户端

spring-consumer.xml：

```xml
<dubbo:application name="demo-consumer"/>

<dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:2181"/>
<!-- 服务方引用的beanId -->
<dubbo:reference id="demoService" check="true" interface="org.apache.dubbo.samples.basic.api.DemoService"/>
```



### 3.2、springboot配置

#### 1、服务端

上面例子的 spring-provider.xml 换成 properties 文件的写法是这样的：

```properties
# 应用名
dubbo.application.name=demo-provider
# 注册中心地址
dubbo.registry.address=zookeeper://localhost:2181
# 调用协议地址
dubbo.protocol.name=dubbo
dubbo.protocol.port=28080
#开启包扫描，可替代 @EnableDubbo 注解
#dubbo.scan.base-packages=com.meizui.quickgame
```

开启基于注解的dubbo功能（主要是包扫描`@DubboComponentScan`）：

```java
// 开启基于注解的dubbo功能（主要是包扫描@DubboComponentScan）
// 也可以在配置文件中使用dubbo.scan.base-package来替代@EnableDubbo
@EnableDubbo(scanBasePackages = {"com.meizui.quickgame"})
@SpringBootApplication
public class UserServiceProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceProviderApplication.class, args);
    }
}
```



#### 2、客户端

```properties
# 应用名
dubbo.application.name=demo-consumer
# 注册中心地址
dubbo.registry.address=zookeeper://localhost:2181
dubbo.consumer.timeout=1000
```



#### 使用例子1：

dubbo和spring可以完美结合，只需要在spring配置文件声明即可。

provider：

```java
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-demo-provider.xml");
        context.start();
        new CountDownLatch(1).await();
    }
```

consumer：

```java
public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/dubbo-demo-consumer.xml");
        context.start();
        DemoService demoService = (DemoService) context.getBean("demoService");
        String hello = demoService.sayHello("world");
}
```



#### 使用例子2：

这里展示注解的方式，需要声明`@EnableDubbo`

provider：

```java
@DubboService(version = "1.0.0")
public class UserServiceImpl implements UserService{
    
}
```

consumer：

```java
@DubboReference(version = "*", protocol = "dubbo,hessian", loadbalance = "random" )
private UserService userService;
```



## 4、其他

### 4.1、负载均衡：

xml:

服务端级别：

```xml
<dubbo:service interface="org.apache.dubbo.samples.basic.api.DemoService" ref="demoService" loadbalance="roundrobin" timeout="5000"/>
```

服务端方法级别：

```xml
<dubbo:service interface="org.apache.dubbo.samples.basic.api.DemoService" ref="demoService" timeout="5000">
	<dubbo:method name="sayHello" loadbalance="roundrobin"/>
</dubbo:service>
```

客户端级别：

```xml
<dubbo:reference id="demoService" check="true" interface="org.apache.dubbo.samples.basic.api.DemoService" loadbalance="roundrobin"/>
```

客户端方法级别：

```xml
<dubbo:reference id="demoService" check="true" interface="org.apache.dubbo.samples.basic.api.DemoService">
	<dubbo:method name="sayHello" loadbalance="roundrobin"/>
</dubbo:reference>
```

**当多个provider注册到zk**，consumer会选择指定的负载均衡算法自动请求，遇到上线或者下线会重新计算。

常见有四种负载均衡：

- **random**

随机，按权重设置随机概率。
在一个截面上碰撞的概率高，但调用量越大分布越均匀，而且按概率使用权重后也比较均匀，有利于动态调整提供者权重。

- **roundRobin** （默认）

轮询，按公约后的权重设置轮询比率。
存在慢的提供者类即请求问题，比如：第二台机器很慢，但没挂，当请求调到第二台时就卡在那，久而久之，所有请求都卡在调到第二台上。


- **leastActive**

最少活跃调用数，相同活跃数的随机，活跃数指调用前后计数差。
使慢的提供者收到更少请求，因为越慢的提供者的调用前后计数差会越大。


- **consistentHash**

一致性Hash，相同参数的请求总是发到同一提供者。
当某一台提供者挂时，原本发往该提供者的请求，基于虚拟节点，平摊到其他提供者，不会引起剧烈变动

### 4.2多协议：

dubbo支持多种协议：

- dubbo
- hessian
- http
- rmi
- webservice
- thrift
- memcached
- redis

#### 不同服务不同协议

不同服务在性能上适用不同协议进行传输，比如大数据用短连接协议，小数据大并发用长连接协议

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.3.xsd http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd"> 
    <dubbo:application name="world"  />
    <dubbo:registry id="registry" address="10.20.141.150:9090" username="admin" password="hello1234" />
    <!-- 多协议配置 -->
    <dubbo:protocol name="dubbo" port="20880" />
    <dubbo:protocol name="rmi" port="1099" />
    <!-- 使用dubbo协议暴露服务 -->
    <dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService" protocol="dubbo" />
    <!-- 使用rmi协议暴露服务 -->
    <dubbo:service interface="com.alibaba.hello.api.DemoService" version="1.0.0" ref="demoService" protocol="rmi" /> 
</beans>
```

#### 多协议暴露服务

需要与 http 客户端相互操作

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:dubbo="http://dubbo.apache.org/schema/dubbo"
    xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.3.xsd http://dubbo.apache.org/schema/dubbo http://dubbo.apache.org/schema/dubbo/dubbo.xsd">
    <dubbo:application name="world"  />
    <dubbo:registry id="registry" address="10.20.141.150:9090" username="admin" password="hello1234" />
    <!-- 多协议配置 -->
    <dubbo:protocol name="dubbo" port="20880" />
    <dubbo:protocol name="hessian" port="8080" />
    <!-- 使用多个协议暴露服务 -->
    <dubbo:service id="helloService" interface="com.alibaba.hello.api.HelloService" version="1.0.0" protocol="dubbo,hessian" />
</beans>
```

### 4.3、多注册中心

两个不同注册中心，使用竖号分隔多个不同注册中心地址：

```xml
<!-- 多注册中心配置，竖号分隔表示同时连接多个不同注册中心，同一注册中心的多个集群地址用逗号分隔 -->
<dubbo:registry address="10.20.141.150:9090|10.20.154.177:9010" />
<!-- 引用服务 -->
<dubbo:reference id="helloService" interface="com.alibaba.hello.api.HelloService" version="1.0.0" />
```



不同服务使用不同注册中心：

```xml
<!-- 多注册中心配置 -->
<dubbo:registry id="chinaRegistry" address="10.20.141.150:9090" />
<dubbo:registry id="intlRegistry" address="10.20.154.177:9010" default="false" />
<!-- 向中文站注册中心注册 -->
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService" registry="chinaRegistry" />
<!-- 向国际站注册中心注册 -->
<dubbo:service interface="com.alibaba.hello.api.DemoService" version="1.0.0" ref="demoService" registry="intlRegistry" />
```

还可以这样：

```xml
<!-- 多注册中心配置 -->
<dubbo:registry id="chinaRegistry" address="10.20.141.150:9090" />
<dubbo:registry id="intlRegistry" address="10.20.154.177:9010" default="false" />
<!-- 引用中文站服务 -->
<dubbo:reference id="chinaHelloService" interface="com.alibaba.hello.api.HelloService" version="1.0.0" registry="chinaRegistry" />
<!-- 引用国际站站服务 -->
<dubbo:reference id="intlHelloService" interface="com.alibaba.hello.api.HelloService" version="1.0.0" registry="intlRegistry" />
```

### 4.4、超时、重试

> 超时默认重试 值： 1000ms 

重试默认重试2次，不算第一个调用，一共会调用三次

如服务方设置：

```xml
<dubbo:service interface="org.apache.dubbo.samples.basic.api.DemoService" ref="demoService2" loadbalance="roundrobin" timeout="500" retries="1"/>
```

> 注意，本人测试了一下，虽然服务方设置重试次数为1，消费者默认，最终结果还是会重试2次（消费者默认值），所以服务方提供的重试次数无效，建议在消费者设置，但是超时时间是生效的。

**超时、重试建议在消费者设置：**

```xml
<dubbo:reference id="demoService01" check="true" interface="org.apache.dubbo.samples.basic.api.DemoService"
        retries="1" timeout="3000"/>
```



### 4.5、分组

当一个接口有多种实现时，可以用 group 区分。

provider.xml

```xml
    <dubbo:service id="groupADubboService" group="groupA" interface="org.apache.dubbo.samples.group.api.GroupService"
                   ref="groupAService"/>

    <dubbo:service id="groupBDubboService" group="groupB" interface="org.apache.dubbo.samples.group.api.GroupService"
                   ref="groupBService"/>
```

consumer.xml

```xml
<!-- * 表示 任意组-->
<dubbo:reference group="*" id="groupAService" check="false"
                     interface="org.apache.dubbo.samples.group.api.GroupService" />
<!-- 调用groupB接口 -->
    <!--<dubbo:reference group="groupB" id="groupBService" check="false"-->
                     <!--interface="org.apache.dubbo.samples.group.api.GroupService"/>-->
```

`group="*"` 和 `group="groupA,groupB"` 是一样的效果，**但总是只调一个可用组的实现**。（不能实现负载均衡）

> 这里有个坑，reference group="*" ，consumer在启动的时候已经选好了分组，并不是轮询两个分组
>
> 如果你要使用group进行负载均衡，consumer就不要写group

### 4.6、版本

当一个接口实现，出现不兼容升级时，可以用版本号过渡，版本号不同的服务相互间不引用。

```xml
<dubbo:reference id="barService" interface="com.foo.BarService" version="1.0.0" />
```

```xml
<dubbo:service interface="com.foo.BarService" version="1.0.0" />
```

>  consumer 设置 version="*" 可以 轮询 调用provider，而group不能，这是和group的最大区别

### 4.6、直连模式

直连模式一般应用于测试模式。

消费者：

```xml
<dubbo:registry id="na" address="N/A" />
<!--使用直连的方式，不需要注册中心 这里的check表示开启token-->
<dubbo:reference id="demoService01" check="true" interface="org.apache.dubbo.samples.basic.api.DemoService"
url="dubbo://172.16.44.48:20897"
protocol="dubbo" registry="na"
timeout="3000"
retries="3"
/>
```

服务方不需要修改，如果开启了token验证，需要在`<dubbo:provider>` 或者 `<dubbo:service>`声明：

```xml
<dubbo:registry id="default" address="zookeeper://${zookeeper.address:127.0.0.1}:2181" />    
<!--使用dubbo协议，声明服务的端口-->
<dubbo:protocol id="dubbo1" name="dubbo" port="20897" />
<dubbo:protocol id="dubbo2" name="dubbo" port="20898"/>
<dubbo:provider token="123456" registry="default">
    <!--用 Spring 配置声明暴露服务 ref 的 值要等 beanId 的 值-->
    <dubbo:service interface="org.apache.dubbo.samples.basic.api.DemoService" ref="demoService02" loadbalance="roundrobin"
                   timeout="5000" retries="1" protocol="dubbo1"/>
</dubbo:provider>
```

消费者需要携带token：

```java
RpcContext.getContext().setAttachment("token","123456");
```

如果不需要token验证，设置成 false 即可



## 5、不同协议的区别

> duboo3.0 的默认协议是 dubbo，较2.0版本来说是一个新的协议（官方称为triple）



![](https://img2018.cnblogs.com/i-beta/1750308/201912/1750308-20191202175008362-1691118481.png)

|          | dubbo                                                        | rmi                                                          | hessian                                                      | http                                                         | webservice                         | thrift                 |
| -------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------------------- | ---------------------- |
| 传输协议 | TCP                                                          | TCP                                                          | HTTP                                                         | HTTP                                                         | HTTP                               | thrif                  |
| 传输方式 | NIO异步传输                                                  | 同步                                                         | 同步                                                         | 同步                                                         | 同步                               | 同步、异步             |
| 序列化   | Hessian二进制序列化                                          | Java标准二进制序列化                                         | Hessian二进制序列化                                          | 表单序列化                                                   | SOAP文本序列化                     | Thrift                 |
| 适用范围 | 传入传出参数数据包较小（建议小于100K），消费者比提供者个数多，单一消费者无法压满提供者，尽量不要使用dubbo协议传输大文件或超大字符串 | 传入传出参数数据包大小混合，消费者与提供者个数差不多，可传文件。 | 传入传出参数数据包较大，提供者比消费者个数多，提供者压力较大，可传文件。 | 传入传出参数数据包大小混合，提供者比消费者个数多，可用浏览器查看，可用表单或URL传入参数，暂不支持传文件。 | 系统集成，跨语言调用。             | 跨语言                 |
| 约束     |                                                              | 参数及返回值需实现Serializable接口                           | 参数及返回值需实现Serializable接口                           |                                                              | 参数及返回值需实现Serializable接口 | 不能在协议中传递null值 |

dubbo 有多种协议，不同的协议默认使用不同的序列化框架。比如：

dubbo 协议 默认使用 Hessian2 序列化。（说明：Hessian2 是阿里在 Hessian 基础上进行的二次开发，起名为Hessian2 ）
rmi协议 默认为 java 原生序列化，

http 协议 默认为 为 json 。

hessian 协议，默认是 hessian 序列化；

webservice 协议，默认是 soap 文本序列化 。



## 6、注意事项（TODO ）

1、使用了dubbo2.7.13 版本，thrift 协议 的name 是native-thrift，而且 `RpcContext.getContext().getAttachments` 无法获取参数

> 当前 dubbo 支持的 thrift 协议是对 thrift 原生协议 的扩展，与原生Thrift不兼容 ，使用的是 `protocol="tri"`
>
> 吐槽一下，dubbo3官方的更新进度也太慢了，本人在使用dubbo 3.0.2.1 的时候，并没有 triple 协议，官方文档也没有找到demo，最后是在dubbo-benchmark项目才知道如何使用。



2、协议的支持

dubbo2.7使用默认的序列化协议是hessian，如果参数是泛型 ，返回值可能为null，此时可以修改序列化为 kryo，并加入：

```xml
<dependency>
    <groupId>com.esotericsoftware</groupId>
    <artifactId>kryo</artifactId>
</dependency>
<dependency>
    <groupId>de.javakaffee</groupId>
    <artifactId>kryo-serializers</artifactId>
</dependency>
```

3、版本的区别

目前dubbo 2.7 版本（包括2.7以下）建议使用dubbo作为RPC调用协议（也是默认）。

本人测试了一下，dubbo 3.0 版本情况下即使按照官方文档，引入 hessian 协议的依赖，仍然报：

```java
No such extension org.apache.dubbo.rpc.Protocol by name hessian, no related exception was found, please check whether related SPI module is missing.
```

暂时没有找到解决方案。

使用dubbo 2.7.13 版本，是可以单独使用hessian协议的。

除此之外本人从网上+测试验证：

- dubbo3.0版本是2021年6月才出来的，新版本默认是使用 trilple 协议，还不兼容旧版本协议
- dubbo3.0官方并没有详细的使用文档，本人测试了一下，与2.7的兼容性还有很大问题，所以建议还是使用2.7版本
- dubbo更新太慢（3年都没有更新3.0版本，捐赠给Apache后基本无维护），网上找到的资料不如springcloud多

4、springboot配置文件

建议springboot接入dubbo不要使用 注解的方式 配置dubbo，统一使用xml文件更友好。

5、关于 dubbo-registry-zookeeper 和 dubbo-dependencies-zookeeper

测试了一下，两个使用其中一个即可，推荐使用 dubbo-registry-zookeeper 

---

参考：

- 使用直连模式：https://blog.csdn.net/hcz666/article/details/115058048
- dubbo官方文档：[https://dubbo.apache.org/zh](https://dubbo.apache.org/zh)




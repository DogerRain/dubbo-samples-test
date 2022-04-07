# dubbo整合SpringBoot例子

dubbo新版本（3.0以上）在相对于 dubbo 旧版本（2.5、2.6、2.7），有很多的不相同的地方。

官方文档也说了新版本的特性：

[https://dubbo.apache.org/zh/docs/v3.0/new-in-dubbo3](https://dubbo.apache.org/zh/docs/v3.0/new-in-dubbo3)

<font style="color:orange;font-size:20px;font-weight:500"> **本文就来使用dubbo3.0新版本 搭建一个dubbo+SpringBoot** 项目，项目结构：</font>

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211119100408971.png)

源码已上传到github：[https://github.com/DogerRain/dubbo-samples-test](https://github.com/DogerRain/dubbo-samples-test)

（项目名字是带springCloud的，因为本来还想整合SpringCloud的，还没来得及整合.....先不改了）

下面是十分详细的过程。

环境要求：

- jdk1.8
- zookeeper 

项目会创建三个modul：

```xml
dubbo-samples-springcloud-api
dubbo-samples-springcloud-consumer
dubbo-samples-springcloud-provider
```

先来创建一个父项目，定义好一些 依赖包和项目结构：

```xml
	<modules>
        <module>dubbo-samples-springcloud-api</module>
        <module>dubbo-samples-springcloud-consumer</module>
        <module>dubbo-samples-springcloud-provider</module>
    </modules>
    <properties>
        <java.version>1.8</java.version>
        <source.level>1.8</source.level>
        <target.level>1.8</target.level>
        <skip_maven_deploy>true</skip_maven_deploy>
        <spring-boot-dependencies.version>2.4.1</spring-boot-dependencies.version>
        <spring-cloud-dependencies.version>Dalston.SR4</spring-cloud-dependencies.version>
        <junit.version>4.12</junit.version>
        <dubbo.version>3.0.2.1</dubbo.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!-- 统一jar版本管理，避免使用 spring-boot-parent -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot-dependencies.version}</version>
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
            <!--dubbo 和  springboot 整合的包-->
            <dependency>
                <groupId>org.apache.dubbo</groupId>
                <artifactId>dubbo-spring-boot-starter</artifactId>
                <version>${dubbo.version}</version>
            </dependency>
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
                <scope>test</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

项目的依赖结构为：

```xml
dubbo-samples-springcloud-consumer
	--dubbo-samples-springcloud-api
	
dubbo-samples-springcloud-provider	
	--dubbo-samples-springcloud-api	

dubbo-samples-springcloud-api	
```



但如果是旧项目不想改动了，可能会不需要API项目那么你的依赖结构就是这样的：

```
dubbo-samples-springcloud-consumer
	--dubbo-samples-springcloud-provider	
	
	
dubbo-samples-springcloud-provider	
```



## 1、创建 API 项目——dubbo-samples-springcloud-api

创建一个 `dubbo-samples-springcloud-api` 项目，创建该项目是为了解耦，这样 provider定义好接口，打成一个SDK，丢给消费者引入就行了。



### 1.1、pom文件：

```xml
	<parent>
        <groupId>com.dubbo</groupId>
        <artifactId>dubbo-samples-springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>

    <artifactId>dubbo-samples-springcloud-api</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>dubbo-samples-springcloud-api</name>
    <description>dubbo stress test - api</description>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.12</version>
        </dependency>
    </dependencies>
```

### 1.2、定义接口

废话少说，先来定义一个接口：

`UserService.java` 接口：

```java
public interface UserService {
    User getUserInfo(long userId);
}
```

`User.java` 实体类：

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = -4294369157631410325L;
    Long userId;
    String userName;
    String responseInfo;
}
```



## 2、创建provider——dubbo-samples-springcloud-provider

创建一个`dubbo-samples-springcloud-provider` 项目

### 2.1、配置pom文件：

```xml
	<parent>
        <groupId>com.dubbo</groupId>
        <artifactId>dubbo-samples-springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    <modelVersion>4.0.0</modelVersion>
    <artifactId>dubbo-samples-springcloud-provider</artifactId>

    <dependencies>
         <!--dubbo-samples-springcloud-api 项目 依赖-->
        <dependency>
            <groupId>com.dubbo</groupId>
            <artifactId>dubbo-samples-springcloud-api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <scope>compile</scope>
        </dependency>
        <!--dubbo 与 spring-boot 整合包-->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
        </dependency>
        <!--springboot 启动核心包-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <!--springboot rest -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-registry-zookeeper</artifactId>
        </dependency>
    </dependencies>
```

使用了 `dubbo-spring-boot-starter` 就不需要 原来的 `dubbo` 依赖了，但是还是需要 `dubbo-registry-zookeeper` ，因为要连接zk。

### 2.2、yaml文件配置

```yaml
server:
  port: 8090

spring:
  application:
    name: dubbo-samples-privider-springCloud

dubbo:
  application:
    name: ${spring.application.name}
  registry:
    address: zookeeper://127.0.0.1:2181
    timeout: 2000
  protocol:
    name: dubbo
    port: 20890
  # 扫描 @DubboService 注解  
  scan:
    base-packages: com.dubbo.service
```

`dubbo.application.name` 可以不写，默认是 `spring.application.name` 的名字。

### 2.3、实现 UserService 接口

```java
@DubboService
@Component
@Slf4j
public class UserServiceImpl implements UserService {

    /**
     *
     * @param userId
     * @return
     * RPC provider 接口 实现
     */
    @Override
    public User getUserInfo(long userId) {

        log.info("request from consumer: {}", RpcContext.getContext().getRemoteAddress());
        log.info("response from provider: {}" , RpcContext.getContext().getLocalAddress());
        return new User(userId, "userName" + userId , " --->>>>response from remote RPC provider:" + RpcContext.getContext().getLocalAddress());
    }
}
```

@DubboService 表示这是一个暴露出去的接口。

> 旧版本可能是 使用 @Service ，与 spring的 @Service 容易让人误解，不推荐使用

@Component 表示这是一个bean，方便其他地方引用

###  2.4、添加一个Controller

我这里用写一个Controller，方便测试。

```java
@RestController
@RequestMapping("/provider")
public class ProviderUserController {

    @Resource
    UserServiceImpl userService;

    @RequestMapping("/user/{id}")
    User getUserInfo(@PathVariable("id") Long id ){
        return userService.getUserInfo(id);
    }
}
```

### 2.5、启动类

```java
@SpringBootApplication
@EnableDubbo
public class ProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
        System.out.println("dubbo client started..........");
    }
}
```

@EnableDubbo 表示自动扫描声明 @DubboService 的类，也可以使用`dubbo.scan.base-packages` 指定扫描路径，两者选择其中一个即可。

## 3、创建consumer——dubbo-samples-springcloud-consumer

### 3.1、配置pom文件

```xml
<parent>
        <groupId>com.dubbo</groupId>
        <artifactId>dubbo-samples-springcloud</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    <artifactId>dubbo-samples-springcloud-consumer</artifactId>
    <packaging>jar</packaging>
    <description>The demo consumer module of dubbo project</description>
    <properties>
        <skip_maven_deploy>true</skip_maven_deploy>
    </properties>

    <dependencies>
        <!--dubbo-samples-springcloud-api 项目 依赖-->
        <dependency>
            <groupId>com.dubbo</groupId>
            <artifactId>dubbo-samples-springcloud-api</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-registry-zookeeper</artifactId>
        </dependency>
    </dependencies>
```

### 3.2、yaml配置

```yaml
server:
  port: 8091

spring:
  application:
    name: dubbo-samples-consumer-springCloud

dubbo:
  registry:
    address: zookeeper://127.0.0.1:2181
    timeout: 2000
  protocol:
    name: dubbo
```

### 3.3、调用 provider

这一步我添加一个Controller 用来调用provider

```java
@RestController
@RequestMapping("/consumer")
@Slf4j
public class ConsumerUserController {

    @DubboReference(version = "*", protocol = "dubbo", loadbalance = "random")
    private UserService userService;

    @RequestMapping("/user/{id}")
    public User getUser(@PathVariable("id") Long id) {
        User user = userService.getUserInfo(id);
        log.info("response from provider: {}", user);
        return user;
    }
}
```

@DubboReference 表示使用RPC（这里使用dubbo协议）进行远程调用。

### 3.4、启动类

```java
@SpringBootApplication
@EnableDubbo
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
        System.out.println("dubbo client started..........");
    }
}
```



## 4、测试

### 4.1、启动 provider

运行 ProviderApplication.java 

端口是 8090

### 4.2、启动 consumer

运行 ConsumerApplication.java 

端口是 8091

### 4.3、 登入 dubboAdmin看看

dubboAdmin 是一个后台可视化项目，可以看到 关于dubbo服务方、消费者相关的信息。

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211119160552651.png)

这里就不阐述怎么搭建了，可以参考其他文章。

> 这里提一下，新版的dubboAdmin不如旧版的dubboAdmin好用，GitHub一堆人在吐槽~

### 4.4、调用 consumer 的接口

浏览器输入：http://localhost:8091/consumer/user/2

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211119150901454.png)

看下provider日志：

```java
2021-11-19 15:05:40.206  INFO 25932 --- [20890-thread-11] com.dubbo.service.UserServiceImpl: request from consumer: /172.16.44.48:50513
2021-11-19 15:05:40.206  INFO 25932 --- [20890-thread-11] com.dubbo.service.UserServiceImpl: response from provider: 172.16.44.48:20890
```

consumer日志：

```java
2021-11-19 15:05:40.207  INFO 33120 --- [nio-8091-exec-4] c.d.controller.ConsumerUserController: response from provider: User(userId=2, userName=userName2, responseInfo= --->>>>response from remote RPC provider:172.16.44.48:20890)
```

你会看到 consumer 启动了一个 50513 的端口和provider进行通讯。

到这里，dubbo+SpringBoot 的整合就完成了。

·

如果你直接调用provider的Controller。

浏览器输入：http://localhost:8090/provider/user/2

它显示是这样的：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211119151331425.png)

它就很普通的调用一样了。

所以在这里你对比一下，RPC也就是这样的一个过程。



## 5、总结和踩坑

### 5.1、版本管理

建议使用dubbo的统一版本管理，也就是 dubbo-bom

### 5.2、jar依赖

如果你使用的是 dubbo-spring-boot-starter ，就不需要 dubbo 这个jar了

### 5.3、关联

正常情况下， 这三个项目不是在一起的。

比如说公司两个部门，一个是用户中心，一个是订单，用户中心要RPC调用订单服务。

那么只需要订单服务只需要新建一个API项目，定义一些接口。

然后打成jar 给到用户中心接入，用户中心使用Dubbo就可以直接调用了。

所以好处就是：

- 不需要使用Http直接调用了（当然Dubbo也支持http协议）
- 不需要整个jar打包，可以解耦，把api项目暴露即可

### 5.4、最后

项目的地址已经上传到github：[https://github.com/DogerRain/dubbo-samples-test](https://github.com/DogerRain/dubbo-samples-test)

接下来还有两个TODO：

- [x] ~~整合xml~~

- [x] ~~使用jmeter、ab压测dubbo、rest、hessian协议 传输大包的性能~~

> 2021年12月17日17:43:38 已完成，参考专栏其他系列文章和GitHub
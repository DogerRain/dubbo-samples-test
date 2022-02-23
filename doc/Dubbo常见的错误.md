以下是一些常见的报错总结（**可以越过不看**）：

### 1、错误一，zk版本低

```java
2021-11-18 09:53:51.972 ERROR 10448 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

An attempt was made to call a method that does not exist. The attempt was made from the following location:

    org.apache.curator.framework.imps.EnsembleTracker.<init>(EnsembleTracker.java:57)

The following method did not exist:

    org.apache.zookeeper.server.quorum.flexible.QuorumMaj.<init>(Ljava/util/Map;)V

The method's class, org.apache.zookeeper.server.quorum.flexible.QuorumMaj, is available from the following locations:

    jar:file:/E:/apache-maven-3.0.5/repositories/org/apache/zookeeper/zookeeper/3.4.13/zookeeper-3.4.13.jar!/org/apache/zookeeper/server/quorum/flexible/QuorumMaj.class

It was loaded from the following location:

    file:/E:/apache-maven-3.0.5/repositories/org/apache/zookeeper/zookeeper/3.4.13/zookeeper-3.4.13.jar


Action:

Correct the classpath of your application so that it contains a single, compatible version of org.apache.zookeeper.server.quorum.flexible.QuorumMaj

2021-11-18 09:53:56.433  INFO 10448 --- [           main] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
```

我本地用的dubbo版本是3.0.2.1，zk版本是3.4.13 

> 因为 dubbo-registry-zookeeper 3.0.2.1 这个jar 依赖了 3.4.13 版本的zk

**解决方法：**

这种情况需要升级zk版本，换成：

```xml
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.6.1</version>
</dependency>
```

dubbo 2.7 版本以上推荐使用 3.6版本的 zk



后面参考了dubbo的官方demo，整了个 dubbo-bom ，所有的版本冲突都解决了，也不需要声明什么版本了，神了：

```xml
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-bom</artifactId>
    <version>${dubbo.version}</version>
    <type>pom</type>
    <scope>import</scope>
</dependency>
```



### 2、配置有误

```java
Error starting ApplicationContext. To display the conditions report re-run your application with 'debug' enabled.
2021-11-18 18:33:46.808 ERROR 21840 --- [           main] o.s.boot.SpringApplication               : Application run failed

java.lang.IllegalStateException: Extension instance (name: service-discovery-registry, class: interface org.apache.dubbo.rpc.Protocol) couldn't be instantiated: null
	at org.apache.dubbo.common.extension.ExtensionLoader.createExtension(ExtensionLoader.java:730) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.common.extension.ExtensionLoader.getExtension(ExtensionLoader.java:495) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.common.extension.ExtensionLoader.getExtension(ExtensionLoader.java:475) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.rpc.Protocol$Adaptive.export(Protocol$Adaptive.java) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.ServiceConfig.doExportUrl(ServiceConfig.java:612) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.ServiceConfig.exportRemote(ServiceConfig.java:586) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.ServiceConfig.exportUrl(ServiceConfig.java:546) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.ServiceConfig.doExportUrlsFor1Protocol(ServiceConfig.java:379) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.ServiceConfig.doExportUrls(ServiceConfig.java:366) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.ServiceConfig.doExport(ServiceConfig.java:342) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.ServiceConfig.export(ServiceConfig.java:233) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.bootstrap.DubboBootstrap.exportServices(DubboBootstrap.java:1411) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.bootstrap.DubboBootstrap.doStart(DubboBootstrap.java:1143) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.bootstrap.DubboBootstrap.start(DubboBootstrap.java:1119) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.spring.context.DubboBootstrapApplicationListener.onContextRefreshedEvent(DubboBootstrapApplicationListener.java:109) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.spring.context.DubboBootstrapApplicationListener.onApplicationContextEvent(DubboBootstrapApplicationListener.java:101) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.apache.dubbo.config.spring.context.DubboBootstrapApplicationListener.onApplicationEvent(DubboBootstrapApplicationListener.java:78) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.doInvokeListener(SimpleApplicationEventMulticaster.java:172) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.invokeListener(SimpleApplicationEventMulticaster.java:165) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.context.event.SimpleApplicationEventMulticaster.multicastEvent(SimpleApplicationEventMulticaster.java:139) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:426) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.context.support.AbstractApplicationContext.publishEvent(AbstractApplicationContext.java:383) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:943) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:591) ~[spring-context-5.3.2.jar:5.3.2]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:144) ~[spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:767) [spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:759) [spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:426) [spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:326) [spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1309) [spring-boot-2.4.1.jar:2.4.1]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1298) [spring-boot-2.4.1.jar:2.4.1]
	at com.dubbo.ProviderApplication.main(ProviderApplication.java:29) [classes/:na]
Caused by: java.lang.reflect.InvocationTargetException: null
	at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method) ~[na:1.8.0_131]
	at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:62) ~[na:1.8.0_131]
	at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:45) ~[na:1.8.0_131]
	at java.lang.reflect.Constructor.newInstance(Constructor.java:423) ~[na:1.8.0_131]
	at org.apache.dubbo.common.extension.ExtensionLoader.createExtension(ExtensionLoader.java:700) ~[dubbo-3.0.2.1.jar:3.0.2.1]
	... 31 common frames omitted
Caused by: java.lang.NoClassDefFoundError: org/apache/curator/framework/listen/ListenerContainer
```

不知道是dubbo3.0的原因 还是 因为SpringBoot yaml 文件配置的原因，省略了 zk 地址的协议或者端口会报错：

```yml
dubbo:
  application:
    name: ${spring.application.name}
  registry:
    address: zookeeper://127.0.0.1:2181
    timeout: 2000
```

**解决方法：**

```yaml
dubbo:
  application:
    name: ${spring.application.name}
  registry:
    address: zookeeper://127.0.0.1:2181
    timeout: 2000
    protocol: -1
    port: -1
```



### 3、低版本的dubbo的依赖

dubbo2.7 如果不使用 dubbo-bom，或者自己整合了zk，可能会报这个错误:

```
java.lang.NoClassDefFoundError: org/apache/curator/framework/recipes/cache/TreeCacheListener
```

在pom文件中加入这两个依赖即可:

```xml
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>4.0.1</version>
</dependency>

<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.8.0</version>
</dependency>
```



### 4、未知错误

```
There's no ApplicationConfig specified
```

spring版本和dubbo版本兼容问题 

参考：

- [https://blog.csdn.net/qq_21187515/article/details/107590592](https://blog.csdn.net/qq_21187515/article/details/107590592)
- [https://cloud.tencent.com/developer/article/1596584](https://cloud.tencent.com/developer/article/1596584)



### 5、

```
Invalid name="org.apache.dubbo.config.ApplicationConfig#0" contains illegal character,
only digit, letter, '-', '_' or '.' is legal.

```

1、 application.yml 、application.properties 配置文件缺失

2、配置文件属性有非法字符
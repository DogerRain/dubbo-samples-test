dubbo3.0整合springboot、spring 的例子。

目前看到网上的大多数例子都是基于dubbo 2.7 的例子，很多注解还停留在 @Service 的基础上

遂作者我整合两个Dubbo3.0 + SpringBoot （注解形式）、Duboo+Spring（xml形式）例子。



## 1、错误一，zk版本低

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



这种情况需要升级zk版本，换成：

```xml
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.6.1</version>
</dependency>
```



dubbo 2.7 版本以上推荐使用 3.6版本的zk



## 2、配置有误

```
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


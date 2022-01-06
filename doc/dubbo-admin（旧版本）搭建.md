Dubbo-admin是官方提供的一个dubbo可视化后台，目前在github找不到这个项目了。

dubbo管理控制台开源部分主要包含： 提供者  路由规则  动态配置  访问控制  权重调节  负载均衡  负责人，等管理功能。

旧版本的dubbo-admin相对新版来说，个人觉得要好用很多~



旧版本的dubbo-admin使用springboot开发，直接下载jar包，执行：

```shell
java -jar -Dserver.port=8848  dubbo-admin-0.0.1-SNAPSHOT.jar
```

即可。

如果要修改zk地址，可以直接打开 jar，建议新增一个 properties 配置文件：

![](https://cdn.jsdelivr.net/gh/DogerRain/image@main/img-202109/image-20211223180813660.png)

比如我这里新增的 `application-test.properties ` 文见，修改为目标zk地址：

```properties
server.port=7001
spring.velocity.cache=false
spring.velocity.charset=UTF-8
spring.velocity.layout-url=/templates/default.vm
spring.messages.fallback-to-system-locale=false
spring.messages.basename=i18n/message
spring.root.password=root
spring.guest.password=guest

dubbo.registry.address=zookeeper://192.34.32.26:2181
```

启动加上profile参数即可：

```shell
java -jar -Dserver.port=8849  dubbo-admin-0.0.1-SNAPSHOT.jar -Dspring.profiles.active=test
```



启动无报错，打开 http://127.0.0.1:8849 即可访问。



附上dubbo-admin 下载地址：[https://github.com/DogerRain/dubbo-samples-test/tree/master/doc/software](https://github.com/DogerRain/dubbo-samples-test/tree/master/doc/software)
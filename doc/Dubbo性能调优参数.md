Dubbo性能调优参数需要根据各自业务进行调整。

建议多在provider端配置属性，原因如下：

- 作为服务的提供方，比服务消费方更清楚服务的性能参数，如调用的超时时间、合理的重试次数等
- 在 Provider 端配置后，Consumer 端不配置则会使用 Provider 端的配置，即 Provider 端的配置可以作为 Consumer 的缺省值 。否则，Consumer 会使用 Consumer 端的全局设置，这对于 Provider 是不可控的，并且往往是不合理的。

配置的覆盖规则：

- 1) 方法级别配置优于接口级别，即小 Scope 优先 
- 2) Consumer 端配置优于 Provider 端配置

## 1、服务端

### 1.1、dubbo:service

```xml
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService"
    timeout="300" retries="2" loadbalance="random" actives="0" />
 
<dubbo:service interface="com.alibaba.hello.api.WorldService" version="1.0.0" ref="helloService"
    timeout="300" retries="2" loadbalance="random" actives="0" >
    <dubbo:method name="findAllPerson" timeout="10000" retries="9" loadbalance="leastactive" actives="5" />
<dubbo:service/>
```

建议在 Provider 端配置的 Consumer 端属性有：

1. `timeout`：方法调用的超时时间

2. `retries`：失败重试次数，缺省是 2 [2](https://dubbo.apache.org/zh/docsv2.7/user/recommend/#fn:2)

3. `loadbalance`：负载均衡算法，

   - 缺省是随机 `random`。

   - 还可以配置轮询 `roundrobin`
   - 最不活跃优先 `leastactive`
   - 一致性哈希 `consistenthash` 

4. `actives`：消费者端的最大并发调用限制，即当 Consumer 对一个服务的并发调用到上限后，新调用会阻塞直到超时，在方法上配置 `dubbo:method` 则针对该方法进行并发限制，在接口上配置 `dubbo:service`，则针对该服务进行并发限制

在 Provider 端配置合理的 Provider 端属性：

```xml
<dubbo:protocol threads="200" /> 
<dubbo:service interface="com.alibaba.hello.api.HelloService" version="1.0.0" ref="helloService"
    executes="200" >
    <dubbo:method name="findAllPerson" executes="50" />
</dubbo:service>
```

建议在 Provider 端配置的 Provider 端属性有：

1. `threads`：服务线程池大小
2. `executes`：一个服务提供者并行执行请求上限，即当 Provider 对一个服务的并发调用达到上限后，新调用会阻塞，此时 Consumer 可能会超时。在方法上配置 `dubbo:method` 则针对该方法进行并发限制，在接口上配置 `dubbo:service`，则针对该服务进行并发限制



### 1.2、dubbo:protocol

dubbo协议缺省端口为`20880`，如果**没有**配置port，则自动采用默认端口，如果配置为**-1**，则会分配一个没有被占用的端口。Dubbo 2.4.0+，分配的端口在协议缺省端口的基础上增长，确保端口段可控。

建议使用固定端口暴露服务，而不要使用随机端口。

这样在注册中心推送有延迟的情况下，消费者通过缓存列表也能调用到原地址，保证调用成功。



### 1.3、dubbo:method

同时该标签为 `<dubbo:service>` 或 `<dubbo:reference>` 的子标签，用于控制到方法级。

| 属性        | 对应URL参数              | 类型    | 是否必填 | 缺省值                           | 作用     | 描述                                                         | 兼容性        |
| ----------- | ------------------------ | ------- | -------- | -------------------------------- | -------- | ------------------------------------------------------------ | ------------- |
| timeout     | <methodName>.timeout     | int     | 可选     | 缺省为的timeout                  | 性能调优 | 方法调用超时时间(毫秒)                                       | 1.0.8以上版本 |
| retries     | <methodName>.retries     | int     | 可选     | 缺省为<dubbo:reference>的retries | 性能调优 | 远程服务调用重试次数，不包括第一次调用，不需要重试请设为0    | 2.0.0以上版本 |
| loadbalance | <methodName>.loadbalance | string  | 可选     | 缺省为的loadbalance              | 性能调优 | 负载均衡策略，可选值：random,roundrobin,leastactive，分别表示：随机，轮询，最少活跃调用 | 2.0.0以上版本 |
| async       | <methodName>.async       | boolean | 可选     | 缺省为<dubbo:reference>的async   | 性能调优 | 是否异步执行，不可靠异步，只是忽略返回值，不阻塞执行线程     | 1.0.9以上版本 |
| sent        | <methodName>.sent        | boolean | 可选     | true                             | 性能调优 | 异步调用时，标记sent=true时，表示网络已发出数据              | 2.0.6以上版本 |
| actives     | <methodName>.actives     | int     | 可选     | 0                                | 性能调优 | 每服务消费者最大并发调用限制                                 | 2.0.5以上版本 |
| executes    | <methodName>.executes    | int     | 可选     | 0                                | 性能调优 | 每服务每方法最大使用线程数限制，此属性只在<dubbo:method>作为<dubbo:service>子标签时有效 | 2.0.5以上版本 |

## 2、客户端

### 2.1、dubbo:consumer

```xml
<dubbo:registry address="zookeeper://${zookeeper.address:127.0.0.1}:${zookeeper.port:2181}"/>
<dubbo:registry id="register2" address="zookeeper://${zookeeper.address:127.0.0.1}:${zookeeper.port:2181}"/>

<dubbo:reference id="userServiceImpl" check="false"
interface="com.meizu.api.UserService" protocol="dubbo" timeout="2000" loadbalance="roundrobin"
retries="3"
/>
<dubbo:consumer id="consumer2" registry="register2" timeout="2000" retries="0" loadbalance="leastactive">
<dubbo:reference id="userServiceImpl2" interface="com.meizu.api.UserService"/>
</dubbo:consumer>
```



| 属性        | 对应URL参数         | 类型    | 是否必填 | 缺省值    | 作用     | 描述                                                         | 兼容性         |
| ----------- | ------------------- | ------- | -------- | --------- | -------- | ------------------------------------------------------------ | -------------- |
| timeout     | default.timeout     | int     | 可选     | 1000      | 性能调优 | 远程服务调用超时时间(毫秒)                                   | 1.0.16以上版本 |
| retries     | default.retries     | int     | 可选     | 2         | 性能调优 | 远程服务调用重试次数，不包括第一次调用，不需要重试请设为0,仅在cluster为failback/failover时有效 | 1.0.16以上版本 |
| loadbalance | default.loadbalance | string  | 可选     | random    | 性能调优 | 负载均衡策略，可选值：random,roundrobin,leastactive，分别表示：随机，轮询，最少活跃调用 | 1.0.16以上版本 |
| async       | default.async       | boolean | 可选     | false     | 性能调优 | 是否缺省异步执行，不可靠异步，只是忽略返回值，不阻塞执行线程 | 2.0.0以上版本  |
| connections | default.connections | int     | 可选     | 100       | 性能调优 | 每个服务对每个提供者的最大连接数，rmi、http、hessian等短连接协议支持此配置，dubbo协议长连接不支持此配置 | 1.0.16以上版   |
| proxy       | proxy               | string  | 可选     | javassist | 性能调优 | 生成动态代理方式，可选：jdk/javassist                        | 2.0.5以上版本  |



### 2.2、dubbo:reference

| 属性        | 对应URL参数 | 类型    | 是否必填 | 缺省值                                | 作用     | 描述                                                         | 兼容性        |
| ----------- | ----------- | ------- | -------- | ------------------------------------- | -------- | ------------------------------------------------------------ | ------------- |
| timeout     | timeout     | long    | 可选     | 缺省使用<dubbo:consumer>的timeout     | 性能调优 | 服务方法调用超时时间(毫秒)                                   | 1.0.5以上版本 |
| retries     | retries     | int     | 可选     | 缺省使用<dubbo:consumer>的retries     | 性能调优 | 远程服务调用重试次数，不包括第一次调用，不需要重试请设为0    | 2.0.0以上版本 |
| connections | connections | int     | 可选     | 缺省使用<dubbo:consumer>的connections | 性能调优 | 对每个提供者的最大连接数，rmi、http、hessian等短连接协议表示限制连接数，dubbo等长连接协表示建立的长连接个数 | 2.0.0以上版本 |
| loadbalance | loadbalance | string  | 可选     | 缺省使用<dubbo:consumer>的loadbalance | 性能调优 | 负载均衡策略，可选值：random,roundrobin,leastactive，分别表示：随机，轮询，最少活跃调用 | 2.0.0以上版本 |
| async       | async       | boolean | 可选     | 缺省使用<dubbo:consumer>的async       | 性能调优 | 是否异步执行，不可靠异步，只是忽略返回值，不阻塞执行线程     | 2.0.0以上版本 |
| actives     | actives     | int     | 可选     | 0                                     | 性能调优 | 每服务消费者每服务每方法最大并发调用数                       | 2.0.5以上版本 |



## 3、注册中心

### 3.1、dubbo:registry

| 属性      | 对应URL参数          | 类型    | 是否必填 | 缺省值 | 作用     | 描述                         | 兼容性        |
| --------- | -------------------- | ------- | -------- | ------ | -------- | ---------------------------- | ------------- |
| transport | registry.transporter | string  | 可选     | netty  | 性能调优 | 网络传输方式，可选mina,netty | 2.0.0以上版本 |
| timeout   | registry.timeout     | int     | 可选     | 5000   | 性能调优 | 注册中心请求超时时间(毫秒)   | 2.0.0以上版本 |
| wait      | registry.wait        | int     | 可选     | 0      | 性能调优 | 停止时等待通知完成时间(毫秒) | 2.0.0以上版本 |
| check     | check                | boolean | 可选     | true   | 服务治理 | 注册中心不存在时，是否报错   | 2.0.0以上版本 |



## 4、dubbo协议

### 4.1、dubbo:protocol

| 属性          | 对应URL参数   | 类型   | 是否必填 | 缺省值                                                       | 作用     | 描述                                                         | 兼容性         |
| ------------- | ------------- | ------ | -------- | ------------------------------------------------------------ | -------- | ------------------------------------------------------------ | -------------- |
| threadpool    | threadpool    | string | 可选     | fixed                                                        | 性能调优 | 线程池类型，可选：fixed/cached                               | 2.0.5以上版本  |
| threads       | threads       | int    | 可选     | 200                                                          | 性能调优 | 服务线程池大小(固定大小)                                     | 2.0.5以上版本  |
| iothreads     | threads       | int    | 可选     | cpu个数+1                                                    | 性能调优 | io线程池大小(固定大小)                                       | 2.0.5以上版本  |
| accepts       | accepts       | int    | 可选     | 0                                                            | 性能调优 | 服务提供方最大可接受连接数                                   | 2.0.5以上版本  |
| payload       | payload       | int    | 可选     | 8388608(=8M)                                                 | 性能调优 | 请求及响应数据包大小限制，单位：字节                         | 2.0.5以上版本  |
| serialization | serialization | string | 可选     | dubbo协议缺省为hessian2，rmi协议缺省为java，http协议缺省为json | 性能调优 | 协议序列化方式，当协议支持多种序列化方式时使用，比如：dubbo协议的dubbo,hessian2,java,compactedjava，以及http协议的json等 | 2.0.5以上版本  |
| queues        | queues        | int    | 可选     | 0                                                            | 性能调优 | 线程池队列大小，当线程池满时，排队等待执行的队列大小，建议不要设置，当线程池满时应立即失败，重试其它服务提供机器，而不是排队，除非有特殊需求。 | 2.0.5以上版本  |
| charset       | charset       | string | 可选     | UTF-8                                                        | 性能调优 | 序列化编码                                                   | 2.0.5以上版本  |
| buffer        | buffer        | int    | 可选     | 8192                                                         | 性能调优 | 网络读写缓冲区大小                                           | 2.0.5以上版本  |
| heartbeat     | heartbeat     | int    | 可选     | 0                                                            | 性能调优 | 心跳间隔，对于长连接，当物理层断开时，比如拔网线，TCP的FIN消息来不及发送，对方收不到断开事件，此时需要心跳来帮助检查连接是否已断开 | 2.0.10以上版本 |


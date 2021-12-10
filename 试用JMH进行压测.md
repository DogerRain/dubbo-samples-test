## 1、JMH简介

`JMH`即`Java Microbenchmark Harness`，是`Java`用来做基准测试的一个工具，该工具由`OpenJDK`提供并维护，测试结果可信度高。

相对于 Jmeter、ab ，它通过编写代码的方式进行压测，在特定场景下会更能评估某项性能。

本次通过使用JMH来压测Dubbo的性能（官方也是使用JMH压测）

## 2、使用

只需要引用两个jar即可：

```xml
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-core</artifactId>
    <version>1.29</version>
</dependency>
<dependency>
    <groupId>org.openjdk.jmh</groupId>
    <artifactId>jmh-generator-annprocess</artifactId>
    <version>1.29</version>
</dependency>
```

通过一系列的注解即可使用JMH。

### @Benchmark

声明一个`public`方法为基准测试方法。该类下的所有被`@Benchmark`注解的方法都会执行。

### @BenchmarkMode

指定测试某个接口的指标，如吞吐量、平均执行时间，一般我都是选择 ALL

```java
@BenchmarkMode({Mode.Throughput,Mode.All})
public class StressTestProvider {

}
```

### @Measurement

用于控制压测的次数

```java
//测量2次，每次测量的持续时间为20秒
@Measurement(iterations = 2, time = 20 , timeUnit = TimeUnit.SECONDS)
```

### @Warmup

预热，预热可以避免首次因为一些其他因素，如CPU波动、类加载耗时这些情况。

```java
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
```

参数解释同上。

### @Fork

`@Fork`用于指定`fork`出多少个子进程来执行同一基准测试方法。

### @Threads

`@Threads`注解用于指定使用多少个线程来执行基准测试方法，如果使用`@Threads`指定线程数为`2`，那么每次测量都会创建两个线程来执行基准测试方法。



## 报错信息：

坑1：

```
ERROR: transport error 202: connect failed: Connection refused ERROR
```

不要使用debug模式执行，使用run模式。



---

参考：

- dubbo压测的官方代码：https://github.com/apache/dubbo-benchmark
- RPC框架压测代码：https://github.com/hank-whu/rpc-benchmark
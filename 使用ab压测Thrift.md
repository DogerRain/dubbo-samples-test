压测思路：

压测consumer的Controller，触发调用，调用provider暴露的接口。

provider做1w次循环，生成随机数做累加。

provider再把consumer的入参无处理返回给consumer。

## 2、ab压测情况

准备工作

机器：

```
consumer1  2h4g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer2  4h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
provider   2h4g	  -server -Xmx2g -Xms2g -XX:+UseG1GC 
```

provide机器情况：

```
2h4g

CentOS release 6.4 (Final)
model name      : QEMU Virtual CPU version 2.5+
stepping        : 3
cpu MHz         : 2099.998
cache size      : 4096 KB
```

Thrift的模型：

非阻塞模型，也是性能最高的

```
TNonblockingServer + TThreadedSelectorServer
```

### 1k 数据

#### 情况一：

consumer1

```
Concurrency Level:      10
Time taken for tests:   89.411 seconds
Complete requests:      100000
Failed requests:        82
   (Connect: 0, Receive: 0, Length: 82, Exceptions: 0)
Write errors:           0
Total transferred:      10900082 bytes
HTML transferred:       400082 bytes
Requests per second:    1118.43 [#/sec] (mean)
Time per request:       8.941 [ms] (mean)
Time per request:       0.894 [ms] (mean, across all concurrent requests)
Transfer rate:          119.05 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:     1    9 154.6      2    5052
Waiting:        1    9 154.6      2    5052
Total:          1    9 154.6      2    5052

Percentage of the requests served within a certain time (ms)
  50%      2
  66%      3
  75%      3
  80%      3
  90%      4
  95%      5
  98%      7
  99%     15
```

consumer2：

```
Concurrency Level:      10
Time taken for tests:   149.157 seconds
Complete requests:      100000
Failed requests:        152
   (Connect: 0, Receive: 0, Length: 152, Exceptions: 0)
Write errors:           0
Total transferred:      10900152 bytes
HTML transferred:       400152 bytes
Requests per second:    670.43 [#/sec] (mean)
Time per request:       14.916 [ms] (mean)
Time per request:       1.492 [ms] (mean, across all concurrent requests)
Transfer rate:          71.37 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       3
Processing:     1   15 214.6      2    5011
Waiting:        1   15 214.6      2    5011
Total:          1   15 214.6      3    5012

Percentage of the requests served within a certain time (ms)
  50%      3
  66%      3
  75%      3
  80%      3
  90%      4
  95%      5
  98%      7
  99%     19
```

请求开始，provider CPU 逐渐升高，数量到了4w后，provider CPU 开始下降，consumer1、consumer2 开始等待，原因应该是 provider 无法处理过多的线程，开始阻塞，拒绝请求。



### 100k数据

## 3、分析



### 坑1：

```
java.net.SocketException: Broken pipe (Write failed)
```

需要先启动 provider，再启动 consumer

### 坑2：

```
org.apache.thrift.transport.TTransportException: MaxMessageSize reached

org.apache.thrift.TApplicationException: helloString failed: out of sequence response: expected 8 but got 4

java.net.SocketException: Software caused connection abort: socket write error
```

不要复用同一个socket，每次调用provider的时候，需要 new Scoket

### 坑3：

```
org.apache.thrift.transport.TTransportException: java.net.BindException: Address already in use: connect
```

windows下端口太少，回收太慢。

consumer需要启动一个socket端口和provider进行通信

### 坑4：

```
java.io.IOException: Too many open files
```

Linux打开句柄文件数太小，设置大一些，`ulimit -n`  查看一下，`ulimit -n 80000 `  临时设置成 80000

#### 坑5：

当请求数过多，大概超过5w请求，使用 TThreadedSelectorServer服务模型

```
   args.selectorThreads(2000);
   args.workerThreads(5000);
   //这里的设置没什么太大用处
```

并发超过了

```
org.apache.thrift.transport.TTransportException: Cannot write to null outputStream

org.apache.thrift.transport.TTransportException: java.net.SocketTimeoutException: connect timed out
```



## 4、结果

服务者机器：

```
2h4g
CentOS release 6.4 (Final)
model name      : QEMU Virtual CPU version 2.5+
stepping        : 3
cpu MHz         : 2099.998
cache size      : 4096 KB
```

JVM：

```
jdk1.8
-server -Xmx2g -Xms2g -XX:+UseG1GC 
```

### 

单机：（没有压满CPU，provider机器同上，consumer2）

```
ab -n 1000000 -c  10 
```

|      | TThreadedSelectorServer + 1k | TThreadedSelectorServer+100k |
| ---- | ---------------------------- | ---------------------------- |
| TPS  | 3700                         | 800                          |
| RTT  | 95% 4ms                      | 95% 16ms                     |
| OOM  | 无                           | 无                           |
| CPU  | 125%+                        | 120%+                        |



Thrift 对并发的支持一般，主要还是要选择合适的Server模型。

超过10w请求，普遍超时。


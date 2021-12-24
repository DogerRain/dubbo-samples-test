## 1、准备

使用ab（Apache-BenchMark）压测consumer的Controller，触发调用，调用provider暴露的接口。

provider做1w次循环，生成随机数做累加。

provider再把consumer的入参无处理返回给consumer。

## 2、ab压测情况

准备工作

机器：

目前只有一台provider，consumer视情况而定（见下面）

```shell
consumer1  8h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer2  4h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
provider   2h4g	  -server -Xmx2g -Xms2g -XX:+UseG1GC 
```

provide机器情况：

```shell
2h4g

CentOS release 6.4 (Final)
model name      : QEMU Virtual CPU version 2.5+
stepping        : 3
cpu MHz         : 2099.998
cache size      : 4096 KB
```

Thrift的模型：

```
TNonblockingServer + TThreadedSelectorServer
```

> 非阻塞模型，也是性能最高的，worker、Excutor 使用默认参数

### 2.1、1k 数据

#### 情况一：

##### 单机

单机consumer1请求provider，不压满CPU

```shell
Time taken for tests:   240.957 seconds
Complete requests:      1000000
Failed requests:        0
Write errors:           0
Total transferred:      109000000 bytes
HTML transferred:       4000000 bytes
Requests per second:    4150.12 [#/sec] (mean)
Time per request:       2.410 [ms] (mean)
Time per request:       0.241 [ms] (mean, across all concurrent requests)
Transfer rate:          441.76 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       2
Processing:     1    2   4.6      2     310
Waiting:        1    2   4.5      2     310
Total:          1    2   4.6      2     311

Percentage of the requests served within a certain time (ms)
  50%      2
  66%      2
  75%      3
  80%      3
  90%      3
  95%      3
  98%      4
  99%      4
```

cpu负载：

```shell
top - 16:18:28 up 1148 days, 31 min,  3 users,  load average: 2.25, 2.47, 2.14
Tasks:   1 total,   0 running,   1 sleeping,   0 stopped,   0 zombie
Cpu0  :  0.3%us,  0.0%sy,  0.0%ni, 99.7%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Cpu1  :  0.0%us,  0.0%sy,  0.0%ni,100.0%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  3890444k used,    34236k free,    42940k buffers
Swap:  2097144k total,     5940k used,  2091204k free,   838524k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
22344 root      20   0 11.6g 2.3g  12m S  0.0 62.4  89:56.09 java -Dfile.encoding=utf-8 -server -Xmx2g -Xms2g -XX:+UseG1GC
```



##### 压满CPU

加多一台机器，以压满provider的CPU，consumer1、consumer2 同时请求provider

consumer1：

```
ab -n 100000 -c  10 http://127.0.0.1:7998/consumer/stress/string1k
```



```shell
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
ab -n 100000 -c  10 http://127.0.0.1:7998/consumer/stress/string1k
```



```shell
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

请求开始，provider CPU 逐渐升高（CPU在171%左右），数量到了4w后，provider CPU 开始下降，降为 17%，provider开始阻塞，consumer1、consumer2 开始等待，并抛出 `connection time out` 原因应该是 provider 无法处理过多的线程，开始阻塞，拒绝请求。

```
org.apache.thrift.transport.TTransportException: Cannot write to null outputStream
```

provider 处理完毕，开始接收请求，CPU再次升高，再下降，反复。



这里我再切换参数试一下：

```java
args.selectorThreads(1000);
args.workerThreads(5000);
LinkedBlockingDeque queue = new LinkedBlockingDeque<>(1024);
ExecutorService executorService = new ThreadPoolExecutor(100, 500,
	60, TimeUnit.SECONDS, queue,
     r -> {
          hread thread = new Thread(r);
          //设置线程异常处理器
          log.error("线程池捕捉错误：", e);
          });
 	return thread;
		}		
);
args.executorService(executorService);
```

并没有什么用，该阻塞还是阻塞。

这样很难测试单机情况下Thrift的性能，决定使用单机 consumer1 500个并发测试一下：

```
ab -n 100000 -c  500 http://127.0.0.1:7998/consumer/stress/string1k
```

consumer1的情况：

```shell
Concurrency Level:      500
Time taken for tests:   21.589 seconds
Complete requests:      100000
Failed requests:        0
Write errors:           0
Total transferred:      10900000 bytes
HTML transferred:       400000 bytes
Requests per second:    4631.91 [#/sec] (mean)
Time per request:       107.947 [ms] (mean)
Time per request:       0.216 [ms] (mean, across all concurrent requests)
Transfer rate:          493.04 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%     71
  66%     76
  75%     81
  80%     84
  90%     91
  95%    103
  98%   1059
  99%   1084
```

tps可以去到 4600，但是RTT 变得很高。

provider的cpu差不多满了：

```
top - 09:57:09 up 1148 days, 18:10,  3 users,  load average: 5.55, 3.43, 1.71
Tasks: 114 total,   1 running, 112 sleeping,   0 stopped,   1 zombie
Cpu0  : 51.1%us, 19.3%sy,  0.0%ni,  6.9%id,  0.0%wa,  0.0%hi, 22.6%si,  0.0%st
Cpu1  : 75.1%us, 17.4%sy,  0.0%ni,  7.5%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  2571440k used,  1353240k free,    84636k buffers
Swap:  2097144k total,    64100k used,  2033044k free,   377692k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
25203 root      20   0 5813m 1.6g  13m S 177.8 43.6  10:20.23 java  
```

分别再测试50、 100、200、300、1000、2000 并发

|          | TPS  | RTT        | provider CPU |
| -------- | ---- | ---------- | ------------ |
| 50并发   | 4600 | 95%     16 | 150%~170%    |
| 100并发  | 4600 | 95%     25 | 160%+        |
| 200并发  | 4600 | 95%     42 | 170%+        |
| 300并发  | 4700 | 95%     62 | 160%~180%    |
| 500并发  | 4800 | 95%    103 | 160%~190%    |
| 1000并发 | 4800 | 95%    336 | 170%~190%    |
| 2000并发 | 4800 | 95%    608 | 180%~190%    |

（使用默认的args参数，tps会高一点）

### 2.2、100k数据

### 情况一

使用一台consumer请求一台provider。

##### 单机

100w请求，10并发

```
ab -n 1000000 -c  10 http://127.0.0.1:7998/consumer/stress/string100k
```

结果：

```
Concurrency Level:      10
Time taken for tests:   1207.079 seconds
Complete requests:      1000000
Failed requests:        0
Write errors:           0
Total transferred:      109000000 bytes
HTML transferred:       4000000 bytes
Requests per second:    828.45 [#/sec] (mean)
Time per request:       12.071 [ms] (mean)
Time per request:       1.207 [ms] (mean, across all concurrent requests)
Transfer rate:          88.18 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       2
Processing:     6   12   6.8     11     116
Waiting:        6   12   6.8     11     116
Total:          6   12   6.8     11     117

Percentage of the requests served within a certain time (ms)
  50%     11
  66%     12
  75%     13
  80%     13
  90%     14
  95%     16
  98%     20
  99%     59
```

provider负载情况：

```shell
top - 16:09:32 up 1148 days, 22 min,  3 users,  load average: 3.24, 2.91, 2.06
Tasks:   1 total,   0 running,   1 sleeping,   0 stopped,   0 zombie
Cpu0  : 34.2%us, 12.2%sy,  0.0%ni, 27.1%id,  0.0%wa,  0.0%hi, 26.4%si,  0.0%st
Cpu1  : 40.3%us,  6.5%sy,  0.0%ni, 53.2%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  3891908k used,    32772k free,    48064k buffers
Swap:  2097144k total,     5940k used,  2091204k free,   838876k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
22344 root      20   0 11.6g 2.3g  12m S 112.5 62.3  82:13.22 java -Dfile.encoding=utf-8 -server -Xmx2g -Xms2g -XX:+UseG1GC
```

##### 压满CPU

使用两台consumer请求一台provider。

provider 运行一段时间后拒绝请求，consumer开始阻塞。

情况和 1k 一样。



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

### 坑5：

当多机器（两台）请求数过多，大概超过4w请求，使用 TThreadedSelectorServer服务模型：

```
   args.selectorThreads(2000);
   args.workerThreads(5000);
   //这里的设置也没什么太大用处
```

超过了4w左右

```
org.apache.thrift.transport.TTransportException: Cannot write to null outputStream

org.apache.thrift.transport.TTransportException: java.net.SocketTimeoutException: connect timed out
```

这个问题找了很久，原因是 client 是非线程安全的，解决方法是：

- 对每一个线程请求都new 一个 socket
- 请求的时候使用锁（推荐）

所以多机请求的时候，可能就因为并发的问题，但是单机并发为什么没有出现呢，应该是每一次请求Controller 都是一个单独的线程（spring默认是Singleton），也就不存在并发问题。

加锁后，1k数据 tps达到了1.5w（同服务器同kvm）
## 4、结果

### 汇总

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



单机：（没有压满CPU，provider机器同上，使用 consumer2压测）

```
ab -n 1000000 -c  10 
```

|      | TThreadedSelectorServer + 1k | TThreadedSelectorServer+100k |
| ---- | ---------------------------- | ---------------------------- |
| TPS  | 3300~4000                    | 800左右                      |
| RTT  | 95% 4ms                      | 95% 16ms                     |
| OOM  | 无                           | 无                           |
| CPU  | 125%+                        | 120%+                        |

多机：

|      | TThreadedSelectorServer + 1k              | TThreadedSelectorServer+100k |
| ---- | ----------------------------------------- | ---------------------------- |
| TPS  | 1200                                      |                              |
| RTT  | Consume1：95% 20ms （consumer2 可能超时） |                              |
| OOM  | 无                                        |                              |
| CPU  | 179% —> 3%                                |                              |



Thrift 对并发的支持一般，主要还是要选择合适的Server模型，阻塞和非阻塞模型的tps区别很大，非阻塞模型 TThreadedSelectorServer  的线程设置参数对tps、RT影响也不大。



### Q1:为什么多机请求普遍超时，单机100w请求不会？

猜测是provider的线程模型不支持同时处理这么多的请求（线程），而单机发出的请求全部进入队列




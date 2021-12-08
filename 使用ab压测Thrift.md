压测思路：

压测consumer的Controller，触发调用，调用provider暴露的接口。

provider做1w次循环，生成随机数做累加。

provider再把consumer的入参无处理返回给consumer。

## 1、ab压测情况

### 1k 数据



#### 情况一：

机器：

```
consumer1  2h4g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer2  4h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
provider   2h4g	  -server -Xmx2g -Xms2g -XX:+UseG1GC 
```

> 因为单机无法让provider机器cpu压满

取10并发，consumer2 进行100w次请求，consumer1 进行 80w次请求 。

```shell
 ab -n 1000000 -c  10 http://127.0.0.1:8091/consumer/stressTest/string1k
```

provider情况：

```shell
top - 09:51:00 up 1137 days, 18:04,  4 users,  load average: 7.07, 3.55, 2.35
Tasks:   1 total,   0 running,   1 sleeping,   0 stopped,   0 zombie
Cpu0  : 80.4%us, 11.0%sy,  0.0%ni,  1.0%id,  0.0%wa,  0.0%hi,  7.6%si,  0.0%st
Cpu1  : 88.6%us,  9.4%sy,  0.0%ni,  2.0%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  3124704k used,   799976k free,   102508k buffers
Swap:  2097144k total,     6156k used,  2090988k free,  1074068k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
 1089 root      20   0 4905m 1.5g  13m S 196.8 39.3  49:05.17 java -Dfile.encoding=utf-8 -server -Xmx2g -Xms2g -XX:+UseG1GC -Xloggc:/data/dubboStress/logs/dubbo_gc.log -XX
```

 可以看到CPU负载过高。

consumer1、consumer2 几乎同时执行结束。

consumer1  2h4g 结果：

```shell
Concurrency Level:      10
Time taken for tests:   197.835 seconds
Complete requests:      800000
Failed requests:        0
Write errors:           0
Total transferred:      87200000 bytes
HTML transferred:       3200000 bytes
Requests per second:    4043.77 [#/sec] (mean)
Time per request:       2.473 [ms] (mean)
Time per request:       0.247 [ms] (mean, across all concurrent requests)
Transfer rate:          430.44 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%      2
  66%      3
  75%      3
  80%      3
  90%      4
  95%      4
  98%      5
  99%      6
 100%    224 (longest request)
```

consumer2  4h8g：

```shell
Concurrency Level:      10
Time taken for tests:   220.944 seconds
Complete requests:      1000000
Failed requests:        0
Write errors:           0
Total transferred:      109000000 bytes
HTML transferred:       4000000 bytes
Requests per second:    4526.03 [#/sec] (mean)
Time per request:       2.209 [ms] (mean)
Time per request:       0.221 [ms] (mean, across all concurrent requests)
Transfer rate:          481.77 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%      2
  66%      2
  75%      3
  80%      3
  90%      3
  95%      4
  98%      4
  99%      5
 100%    261 (longest request)
```

可以看到 两台机tps加起来有 8500多，95%响应时间都在 5ms以内。

#### 情况二：

我再加一台机器测试一下：

```
consumer1  2h4g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer2  4h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer3  2h4g   -server -Xmx2g -Xms2g -XX:+UseG1GC （跨网段）
provider   2h4g	  -server -Xmx2g -Xms2g -XX:+UseG1GC 
```

consumer1：

```shell
Concurrency Level:      10
Time taken for tests:   269.711 seconds
Complete requests:      1000000
Failed requests:        0
Write errors:           0
Total transferred:      109000000 bytes
HTML transferred:       4000000 bytes
Requests per second:    3707.68 [#/sec] (mean)
Time per request:       2.697 [ms] (mean)
Time per request:       0.270 [ms] (mean, across all concurrent requests)
Transfer rate:          394.66 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%      2
  66%      3
  75%      3
  80%      3
  90%      4
  95%      5
  98%      5
  99%      6
 100%    233 (longest request)
```

consumer2：

```shell
Concurrency Level:      10
Time taken for tests:   251.411 seconds
Complete requests:      1000000
Failed requests:        0
Write errors:           0
Total transferred:      109000000 bytes
HTML transferred:       4000000 bytes
Requests per second:    3977.55 [#/sec] (mean)
Time per request:       2.514 [ms] (mean)
Time per request:       0.251 [ms] (mean, across all concurrent requests)
Transfer rate:          423.39 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       2
Processing:     1    2   2.8      2     201
Waiting:        0    2   2.8      2     201
Total:          1    2   2.8      2     201

Percentage of the requests served within a certain time (ms)
  50%      2
  66%      3
  75%      3
  80%      3
  90%      4
  95%      4
  98%      5
  99%      6
 100%    201 (longest request)
```

consumer3：

（跨机房，RTT较慢）

```shell
Concurrency Level:      10
Time taken for tests:   253.211 seconds
Complete requests:      220941
Failed requests:        0
Write errors:           0
Total transferred:      24082569 bytes
HTML transferred:       883764 bytes
Requests per second:    872.56 [#/sec] (mean)
Time per request:       11.461 [ms] (mean)
Time per request:       1.146 [ms] (mean, across all concurrent requests)
Transfer rate:          92.88 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%     11
  66%     11
  75%     12
  80%     12
  90%     13
  95%     14
  98%     15
  99%     17
 100%    281 (longest request)
```

可以看到tps累加也差不多是 8500多。

#### 情况三：

```
consumer1  2h4g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer2  4h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer3  8h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
provider   2h4g	  -server -Xmx2g -Xms2g -XX:+UseG1GC 
```

结果差不多，TPS有增加，误差来自于 consumer1执行完成，consumer2、consumer3还没执行完成，但是时间差在5秒以内。

consumer1：

```shell
Concurrency Level:      10
Time taken for tests:   299.224 seconds
Complete requests:      1000000
Requests per second:    3341.98 [#/sec] (mean)
Transfer rate:          355.74 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%      3
  66%      3
  75%      3
  80%      4
  90%      4
  95%      5
  98%      6
  99%      7
```

consmer2：

```shell
Concurrency Level:      10
Time taken for tests:   339.512 seconds
Complete requests:      1000000
Requests per second:    2945.41 [#/sec] (mean)
Transfer rate:          313.52 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%      3
  66%      4
  75%      4
  80%      4
  90%      5
  95%      6
  98%      7
  99%      8
```

consmer3：

```shell
Concurrency Level:      10
Time taken for tests:   368.969 seconds
Complete requests:      1000000
Requests per second:    2710.26 [#/sec] (mean)
Transfer rate:          288.49 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%      3
  66%      4
  75%      4
  80%      5
  90%      5
  95%      6
  98%      7
  99%      8
```

TPS差不多近9000，RTT响应时间差不多。

provider 服务器情况：

```shell
top - 17:53:54 up 1141 days,  2:07,  2 users,  load average: 7.14, 5.33, 4.19
Tasks:   1 total,   0 running,   1 sleeping,   0 stopped,   0 zombie
Cpu0  : 84.3%us,  8.7%sy,  0.0%ni,  0.7%id,  0.0%wa,  0.0%hi,  6.4%si,  0.0%st
Cpu1  : 89.4%us,  9.6%sy,  0.0%ni,  1.0%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  3889636k used,    35044k free,   135212k buffers
Swap:  2097144k total,     5988k used,  2091156k free,  1784112k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
 1089 root      20   0 4908m 1.5g  13m S 198.1 39.4 114:41.32 java -Dfile.encoding=utf-8 -server -Xmx2g -Xms2g -XX:+UseG1GC -Xloggc:/data/dubboStress/logs/dubbo_gc.log
```

### 2、100k数据

#### 情况一：

```
consumer1  2h4g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer2  8h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
provider   2h4g	  -server -Xmx2g -Xms2g -XX:+UseG1GC 
```

10w请求，20并发

```
ab -n 100000 -c 20 http://127.0.0.1:8091/consumer/stressTest/string100k
```

consumer1：

```shell
Concurrency Level:      20
Time taken for tests:   184.131 seconds
Requests per second:    543.09 [#/sec] (mean)
Transfer rate:          57.81 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%     36
  66%     38
  75%     39
  80%     40
  90%     42
  95%     44
  98%     48
  99%     50
```

consumer2:

```shell
Concurrency Level:      20
Time taken for tests:   185.186 seconds
Complete requests:      100000
Requests per second:    540.00 [#/sec] (mean)
Transfer rate:          57.48 [Kbytes/sec] received

Percentage of the requests served within a certain time (ms)
  50%     36
  66%     38
  75%     39
  80%     40
  90%     42
  95%     45
  98%     49
  99%     52
```

tps差不多1000，RTT 95% 45ms

#### 情况二：

```
consumer1  2h4g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer2  8h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
provider   2h4g	  -server -Xmx2g -Xms2g -XX:+UseG1GC 
```

10w请求 10并发

```shell
ab -n 100000 -c 10 http://127.0.0.1:8091/consumer/stressTest/string100k
```

consumer1：

```shell
Concurrency Level:      10
Time taken for tests:   184.276 seconds
Complete requests:      100000
Failed requests:        0
Write errors:           0
Total transferred:      10900000 bytes
HTML transferred:       400000 bytes
Requests per second:    542.66 [#/sec] (mean)
Time per request:       18.428 [ms] (mean)
Time per request:       1.843 [ms] (mean, across all concurrent requests)
Transfer rate:          57.76 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:     7   18   2.8     18      67
Waiting:        7   18   2.8     18      67
Total:          7   18   2.8     18      67

Percentage of the requests served within a certain time (ms)
  50%     18
  66%     19
  75%     20
  80%     20
  90%     21
  95%     23
  98%     25
  99%     28
```

consumer2：

```shell
Concurrency Level:      10
Time taken for tests:   183.730 seconds
Complete requests:      100000
Failed requests:        0
Write errors:           0
Total transferred:      10900000 bytes
HTML transferred:       400000 bytes
Requests per second:    544.28 [#/sec] (mean)
Time per request:       18.373 [ms] (mean)
Time per request:       1.837 [ms] (mean, across all concurrent requests)
Transfer rate:          57.94 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:     7   18   2.8     18      56
Waiting:        7   18   2.8     18      55
Total:          7   18   2.8     18      56

Percentage of the requests served within a certain time (ms)
  50%     18
  66%     19
  75%     20
  80%     20
  90%     22
  95%     23
  98%     25
  99%     27
```

TPS:1000 ，RTT 95% 23ms

> 说明并不是并发数越高越好。

#### 情况三：

加一台机器。

```shell
consumer1  2h4g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer2  4h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
consumer3  8h8g   -server -Xmx4g -Xms4g -XX:+UseG1GC
provider   2h4g	  -server -Xmx2g -Xms2g -XX:+UseG1GC 
```

10w请求 10并发

```shell
ab -n 100000 -c 10 http://127.0.0.1:8091/consumer/stressTest/string100k
```

> consumer1先执行完成，手动停止 2、3  ，存在2s误差。

consumer1：

```shell
Concurrency Level:      10
Time taken for tests:   179.200 seconds
Complete requests:      100000
Failed requests:        0
Write errors:           0
Total transferred:      10900000 bytes
HTML transferred:       400000 bytes
Requests per second:    558.04 [#/sec] (mean)
Time per request:       17.920 [ms] (mean)
Time per request:       1.792 [ms] (mean, across all concurrent requests)
Transfer rate:          59.40 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:     7   18   4.3     17      55
Waiting:        7   18   4.3     17      54
Total:          7   18   4.3     17      55

Percentage of the requests served within a certain time (ms)
  50%     17
  66%     19
  75%     21
  80%     22
  90%     23
  95%     25
  98%     27
  99%     29
```

consumer2：

```shell
Concurrency Level:      10
Time taken for tests:   190.898 seconds
Complete requests:      73115
Failed requests:        0
Write errors:           0
Total transferred:      7969535 bytes
HTML transferred:       292460 bytes
Requests per second:    383.01 [#/sec] (mean)
Time per request:       26.109 [ms] (mean)
Time per request:       2.611 [ms] (mean, across all concurrent requests)
Transfer rate:          40.77 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:     7   26   5.6     26      67
Waiting:        7   26   5.6     26      66
Total:          7   26   5.6     26      67

Percentage of the requests served within a certain time (ms)
  50%     26
  66%     28
  75%     29
  80%     30
  90%     33
  95%     35
  98%     38
  99%     41
```

consumer3：

```shell
Concurrency Level:      10
Time taken for tests:   188.066 seconds
Complete requests:      44403
Failed requests:        0
Write errors:           0
Total transferred:      4839927 bytes
HTML transferred:       177612 bytes
Requests per second:    236.10 [#/sec] (mean)
Time per request:       42.354 [ms] (mean)
Time per request:       4.235 [ms] (mean, across all concurrent requests)
Transfer rate:          25.13 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:    11   42  13.0     42     182
Waiting:       11   42  13.0     42     181
Total:         11   42  13.0     42     182

Percentage of the requests served within a certain time (ms)
  50%     42
  66%     48
  75%     51
  80%     53
  90%     58
  95%     64
  98%     71
  99%     76
```

tps 1100左右

provider 服务器情况：

```shell
top - 10:18:21 up 1141 days, 18:31,  3 users,  load average: 5.02, 4.67, 4.14
Tasks:   1 total,   0 running,   1 sleeping,   0 stopped,   0 zombie
Cpu0  :  0.1%us,  0.0%sy,  0.0%ni, 99.9%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Cpu1  :  0.1%us,  0.0%sy,  0.0%ni, 99.8%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  3892608k used,    32072k free,   141252k buffers
Swap:  2097144k total,     5988k used,  2091156k free,  1726716k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
 1089 root      20   0 4908m 1.5g  13m S 198.5 40.8 188:01.30 java -Dfile.encoding=utf-8 -server -Xmx2g -Xms2g -XX:+UseG1GC 
```



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

```
org.apache.thrift.transport.TTransportException: java.net.ConnectException: Connection timed out (Connection timed out)
```



## 4、汇总

单机：（没有压满CPU，provider机器同上，consumer2）

|      | TThreadedSelectorServer + 1k | TThreadedSelectorServer+100k |
| ---- | ---------------------------- | ---------------------------- |
| TPS  | 3300                         | 700~800                      |
| RTT  | 95% 4ms                      | 95%  19ms                    |
| OOM  | 无                           | 无                           |
| CPU  | 125%+                        | 150%                         |



Thrift 对并发的支持一般，主要还是要选择合适的Server模型。
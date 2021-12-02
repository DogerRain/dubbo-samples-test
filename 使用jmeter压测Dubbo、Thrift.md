参考：

- 压测dubbo：https://blog.csdn.net/u013815546/article/details/101385888

- 压测dubbo：（有点不一样）https://blog.csdn.net/cyjs1988/article/details/84258046

- https://blog.51cto.com/ydhome/1861956

- 官网：https://jmeter-plugins.org/wiki/Start/

- jmeter使用：https://blog.csdn.net/github_27109687/article/details/71968662

使用 rest 协议：

https://dangdangdotcom.github.io/dubbox/rest.html





服务端的cpu、内存监测，下载：

https://www.cnblogs.com/imyalost/p/7751981.html

jmeter版本和serverAgent版本问题：

https://www.cnblogs.com/SunshineKimi/p/11361216.html

dubbo压测的官方代码：

https://github.com/apache/dubbo-benchmark



博客推荐：

http://shiyanjun.cn/archives/349.html



使用的分析工具：

1、gc可视化工具：https://gceasy.io/

2、压测工具：jmeter（需要安装插件）



序列化：dubbo协议缺省为hessian2，rmi协议缺省为java，http协议缺省为json





servergent：

```
[root@GZSB-CJB-SHH10-8-LASTMILE-32 ServerAgent-2.2.3]# ./startAgent.sh 
INFO    2021-11-25 14:06:18.483 [kg.apc.p] (): Binding UDP to 4444
INFO    2021-11-25 14:06:19.490 [kg.apc.p] (): Binding TCP to 4444
INFO    2021-11-25 14:06:19.497 [kg.apc.p] (): JP@GC Agent v2.2.3 started

```





## 1、Jmeter压测情况

环境：

```
jdk：1.8

2h4g

CentOS release 6.4 (Final)
model name      : QEMU Virtual CPU version 2.5+
stepping        : 3
cpu MHz         : 2099.998
cache size      : 4096 KB
```

宿主机带宽 ：

```
1G
```

jvm参数：

```
-server -Xmx2g -Xms2g -Xmn256m -Xss256k -XX:+UseG1GC
```

jmeter参数：

```
20个并发线程1s内发出，持续10分钟
```



### 1、1k数据

发送1k数据，无处理直接返回。

|                | dubbo2.7.13，dubbo+hessian2                                  | thrift（TThreadPoolServer+TBinaryProtocol） |
| -------------- | ------------------------------------------------------------ | ------------------------------------------- |
| 样本（请求数） | 1298522                                                      |                                             |
| TPS            | 2164.2  ，1min到瓶颈<br />![](picture/image-20211129182941303.png) |                                             |
| 响应时间比例   | 90% 10ms；95% 11ms 99% 18ms，avg 8ms![image-20211129182500367](picture/image-20211129182500367.png) |                                             |
|                | ![](picture/image-20211129182730558.png)                     |                                             |
| CPU            | <br />服务器 CPU：32%，内存 ： 21.6 %![](picture/image-20211129182545191.png) |                                             |
| IO             | ![image-20211129183100390](picture/image-20211129183100390.png) |                                             |
| NetWork        | 2 800 000![](picture/image-20211129183054623.png)            |                                             |
| 吞吐量         | ![](picture/image-20211129183200284.png)                     |                                             |
| gc             | 1 sec 62 ms                                                  |                                             |
|                | ![](picture/image-20211129183915989.png)                     |                                             |
|                | ![](picture/image-20211130092624053.png)                     |                                             |



### 2、100k数据

发送100k数据，无处理直接返回。

|                | dubbo2.7.13+hessian2                                         | thrift |
| -------------- | ------------------------------------------------------------ | ------ |
| 样本（请求数） | 79450                                                        |        |
| TPS            | 132.4 ；<br />![](picture/image-20211130095928574.png)       |        |
| 响应时间比例   | 90% 213ms；95% 227ms ；99% 288ms<br />![](picture/image-20211130095420414.png) |        |
|                | ![](picture/image-20211130095913354.png)                     |        |
| CPU            | %CPU 26.0 ; %MEM 16.5<br />![](picture/image-20211130100005057.png) |        |
| IO             | ![](picture/image-20211130095948793.png)                     |        |
| NetWork        | 1 200 000![](picture/image-20211130095630222.png)            |        |
| 吞吐量         | ![](picture/image-20211130095827620.png)                     |        |
| gc             | 490 ms                                                       |        |
|                | ![](picture/image-20211130100435608.png)                     |        |
|                |                                                              |        |

 

可以看到 100k 数据的时候，dubbo的响应时间很慢、tps也低。

> 中山consumer ping 珠海provider 延迟差不多是7ms
>
> 同机房延迟是 0.2ms



dubbo 1k 和 100k 的top：

- 1k

```
Tasks: 118 total,   1 running, 117 sleeping,   0 stopped,   0 zombie
Cpu0  : 23.6%us, 16.7%sy,  0.0%ni, 57.6%id,  0.3%wa,  0.0%hi,  1.7%si,  0.0%st
Cpu1  :  5.0%us,  7.7%sy,  0.0%ni, 87.3%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  3201900k used,   722780k free,   318184k buffers
Swap:  2097144k total,       64k used,  2097080k free,  1186712k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
19560 root      20   0 3834m 826m  13m S 33.3 21.6   0:43.04 java    
```

- 100k

```
top - 09:49:23 up 528 days, 17:07,  4 users,  load average: 0.16, 0.29, 0.21
Tasks:   1 total,   0 running,   1 sleeping,   0 stopped,   0 zombie
Cpu0  : 16.1%us,  2.1%sy,  0.0%ni, 80.7%id,  0.0%wa,  0.0%hi,  1.1%si,  0.0%st
Cpu1  :  8.1%us,  1.3%sy,  0.0%ni, 90.6%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  2971000k used,   953680k free,   320012k buffers
Swap:  2097144k total,       64k used,  2097080k free,  1185236k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
29635 root      20   0 4713m 632m  12m S 26.0 16.5   3:49.77 java
```

shrift 1k 和 100k 的top：





## 2、ab压测情况

启动三台服务器作为consumer，一台provider 压测。

consumer服务器一：2h4g ：

```
Concurrency Level:      10
Time taken for tests:   306.857 seconds
Complete requests:      1000000
Failed requests:        0
Write errors:           0
Total transferred:      109000000 bytes
HTML transferred:       4000000 bytes
Requests per second:    3258.85 [#/sec] (mean)
Time per request:       3.069 [ms] (mean)
Time per request:       0.307 [ms] (mean, across all concurrent requests)
Transfer rate:          346.89 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.0      0       1
Processing:     1    3   3.0      3     257
Waiting:        1    3   3.0      3     257
Total:          1    3   3.0      3     257

Percentage of the requests served within a certain time (ms)
  50%      3
  66%      3
  75%      4
  80%      4
  90%      5
  95%      5
  98%      7
  99%      8
 100%    257 (longest request)
```

```
Concurrency Level:      10
Time taken for tests:   289.370 seconds
Complete requests:      1000000
Failed requests:        0
Write errors:           0
Total transferred:      109000000 bytes
HTML transferred:       4000000 bytes
Requests per second:    3455.78 [#/sec] (mean)
Time per request:       2.894 [ms] (mean)
Time per request:       0.289 [ms] (mean, across all concurrent requests)
Transfer rate:          367.85 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       9
Processing:     1    3   3.2      2     277
Waiting:        0    3   3.2      2     277
Total:          1    3   3.2      3     277

Percentage of the requests served within a certain time (ms)
  50%      3
  66%      3
  75%      3
  80%      4
  90%      4
  95%      5
  98%      6
  99%      7
 100%    277 (longest request)
```

consumer服务器三：2h4g ：（跨网段）

```
Concurrency Level:      10
Time taken for tests:   458.840 seconds
Complete requests:      402175
Failed requests:        0
Write errors:           0
Total transferred:      43837075 bytes
HTML transferred:       1608700 bytes
Requests per second:    876.50 [#/sec] (mean)
Time per request:       11.409 [ms] (mean)
Time per request:       1.141 [ms] (mean, across all concurrent requests)
Transfer rate:          93.30 [Kbytes/sec] received

Connection Times (ms)
              min  mean[+/-sd] median   max
Connect:        0    0   0.1      0       5
Processing:    10   11   3.3     11     234
Waiting:        9   11   3.3     11     220
Total:         10   11   3.3     11     235

Percentage of the requests served within a certain time (ms)
  50%     11
  66%     11
  75%     12
  80%     12
  90%     13
  95%     14
  98%     16
  99%     18
 100%    235 (longest request)
```



## 3、分析

### 2.1、dubbo线程 WAITING

试过调大了 dubbo （默认是200）的线程数，发现 


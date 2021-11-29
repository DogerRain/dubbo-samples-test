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





## 1、压测情况

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

|                | dubbo2.7.13+hessian2                                         | thrift |
| -------------- | ------------------------------------------------------------ | ------ |
| 样本（请求数） | 1298522                                                      |        |
| TPS            | 2200<br />![image-20211129182941303](picture/image-20211129182941303.png) |        |
| 响应时间比例   | 90% 10ms；95% 11ms 99% 81ms，avg 8ms![image-20211129182500367](picture/image-20211129182500367.png) |        |
|                | ![](picture/image-20211129182730558.png)                     |        |
| CPU            | <br />服务器 32%![](picture/image-20211129182545191.png)     |        |
| IO             | ![image-20211129183100390](picture/image-20211129183100390.png) |        |
| NetWork        | ![image-20211129183054623](picture/image-20211129183054623.png) |        |
| 吞吐量         | ![](picture/image-20211129183200284.png)                     |        |
| gc次数         |                                                              |        |
|                | ![](picture/image-20211129183915989.png)                     |        |
|                |                                                              |        |



### 2、100k数据

### 

|              | dubbo2.7.13+hessian2 | thrift |
| ------------ | -------------------- | ------ |
| 样本         | 314289               |        |
| TPS          | 1200                 |        |
| RTT          | 10.3                 |        |
| 响应时间比例 | 95% 40ms             |        |
| CPU          |                      |        |
| gc次数       |                      |        |
| gc时间       |                      |        |
|              |                      |        |

 





```
Tasks: 118 total,   1 running, 117 sleeping,   0 stopped,   0 zombie
Cpu0  : 23.6%us, 16.7%sy,  0.0%ni, 57.6%id,  0.3%wa,  0.0%hi,  1.7%si,  0.0%st
Cpu1  :  5.0%us,  7.7%sy,  0.0%ni, 87.3%id,  0.0%wa,  0.0%hi,  0.0%si,  0.0%st
Mem:   3924680k total,  3201900k used,   722780k free,   318184k buffers
Swap:  2097144k total,       64k used,  2097080k free,  1186712k cached

  PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                                                                                       
19560 root      20   0 3834m 826m  13m S 33.3 21.6   0:43.04 java    
```


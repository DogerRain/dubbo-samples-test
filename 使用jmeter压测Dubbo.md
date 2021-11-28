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
```

宿主机带宽 ：

```
1G
```

jvm参数：

```
-Xms1024m -Xmx1024m -XX:PermSize=64M -XX:+UseG1GC
```



## 1.1、dubbo+默认


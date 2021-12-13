#!/bin/bash
NAME="dubbo-samples-springcloud-consumer-0.0.1-SNAPSHOT.jar"    #???????
ps_pid=`ps -ef | grep "$NAME" | grep -v grep | awk '{print $2}'`
#kill -9 ${ps_pid}
echo "有进程:$ps_pid"

echo "-------正在杀进程­--------"
for id in $ps_pid
do
kill -9 $id
echo "杀进程: $id"
done
echo "-------杀进程结束--------"
echo "-------重启--------"


echo "gc配置:$GC_settings"

GC_settings="-Xloggc:/data/dubboStress/logs/dubbo_gc.log -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/var/log/heap.bin"
#使用g1，不建议设置-Xmn,如 -Xmn256m ，g1会自动分配
JVM_settings="-server -Xmx4g -Xms4g -XX:+UseG1GC "
#1.6 使用CMS，标志整理（dubbo官方使用）
#JVM_settings_1.6="-server -Xmx2g -Xms2g -Xmn256m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70"


#JMX_setings="-Djava.rmi.server.hostname=10.131.32.26 -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=3214 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false"
JMX_setings=""


cd /data/dubboStress
# 控制台日志文件
CONSOLE_LOG=dubbo_console.log
CONSOLE_ERROR_LOG=dubbo_error_console.log


nohup java -Dfile.encoding=utf-8 $JVM_settings  $GC_settings  -Duser.timezone=Asia/Shanghi $JMX_setings -jar $NAME --spring.profiles.active=test >$CONSOLE_LOG 2>$CONSOLE_ERROR_LOG & 
echo "-------重启结束--------"

# 清空日志，并且打印控制台日志
> $CONSOLE_LOG
> /data/dubboStress/logs/dubbo_gc.log
> /data/dubboStress/dubbo_debug.log
> /data/dubboStress/dubbo_error.log


tail -f /data/dubboStress/$CONSOLE_LOG

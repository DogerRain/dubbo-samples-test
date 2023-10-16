@echo off
set finishTime="%date% %time%"
echo "执行时间：%finishTime%"

java -cp F:\开发任务\Dubbo\dubbo-samples-test\dubbo-samples-springboot\dubbo-samples-springboot-provider\src\main\java\;. com.dubbo.util.ClassCopyUtils

SET msg1=%date:~0,10%
SET msg2=_autocommit_dubbo-samples-test
SET msg=%msg1%%msg2%
echo %msg%
git add .
git commit * -m %msg%
git pull
git push

@cmd.exe

psId=` ps -ef|grep "dubbo-samples-springcloud*" |grep -v grep|cut -c 9-15`
top -p $psId -c 

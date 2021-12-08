package com.thrift; /**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */

import com.thrift.api.Hello;
import com.thrift.config.ThriftConsumerConfiguration;
import org.apache.thrift.TException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class HelloServiceClient {
    /**
     * 调用 Hello 服务
     * @param args
     */

    public static void main(String[] args) throws IOException {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ThriftConsumerConfiguration.class);
        context.start();

        ThriftConsumerConfiguration thriftConsumerConfiguration = (ThriftConsumerConfiguration) context.getBean("thriftConsumerConfiguration");
        // 调用服务的 helloVoid 方法
//            client.helloVoid();
        while (true){
           try{
               System.in.read();
               String resp = thriftConsumerConfiguration.getRemoteResult("wertyuiopsdfghjkl");
               System.out.println(resp);
           }catch (Exception e){
               e.printStackTrace();
           }
        }
    }
}
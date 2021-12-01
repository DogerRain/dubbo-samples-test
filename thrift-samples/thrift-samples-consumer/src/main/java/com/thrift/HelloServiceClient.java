package com.thrift; /**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */

import com.shrift.api.Hello;
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

        try {
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ThriftConsumerConfiguration.class);
            context.start();

            ThriftConsumerConfiguration thriftConsumerConfiguration = (ThriftConsumerConfiguration) context.getBean("thriftConsumerConfiguration");
            Hello.Client client = thriftConsumerConfiguration.client2();
            // 调用服务的 helloVoid 方法
//            client.helloVoid();
            while (true){
               try{
                   System.in.read();
                   String resp = client.helloString("wertyuiopsdfghjkl;");
                   System.out.println(resp);
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
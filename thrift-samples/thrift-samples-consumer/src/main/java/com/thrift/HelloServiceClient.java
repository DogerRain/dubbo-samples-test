package com.thrift; /**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */

import com.shrift.api.Hello;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;

public class HelloServiceClient {
    /**
     * 调用 Hello 服务
     * @param args
     */


    public static void main(String[] args) throws IOException {
        try {
            // 设置调用的服务地址为本地，端口为 7911
            TTransport transport = new TSocket("localhost", 7911);
            transport.open();
            // 设置传输协议为 TBinaryProtocol
            TProtocol protocol = new TBinaryProtocol(transport);
            Hello.Client client = new Hello.Client(protocol);
            // 调用服务的 helloVoid 方法
//            client.helloVoid();
            while (true){
                System.in.read();
                String resp = client.helloString("wertyuiopsdfghjkl;");
                System.out.println(resp);
            }
//            transport.close();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
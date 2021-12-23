package com.thrift.server;

import com.thrift.api.ConfigThrift;
import com.thrift.api.Hello;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.apache.thrift.transport.layered.TFramedTransport;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/30
 * @Description
 */
public class ThriftClientMain {
    public static final String SERVER_IP = ConfigThrift.SERVER_HOSTNAME;
    public static final int SERVER_PORT = ConfigThrift.SOCKET_PORT;
    public static final int TIMEOUT = ConfigThrift.TIMEOUT;

    /**
     * @param userName
     */
    public void startTserverClient(String userName) {
        TTransport transport = null;
        try {
            transport = new TSocket(SERVER_IP, SERVER_PORT);
            transport.open();
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            // TProtocol protocol = new TCompactProtocol(transport);
            // TProtocol protocol = new TJSONProtocol(transport);
            Hello.Client client = new Hello.Client(
                    protocol);
            String result = client.helloString(userName);
            System.out.println("Thrify client result =: " + result);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    public void startTNonblockingServerClient(String userName) {
        TTransport transport = null;
        try {
            transport = new TFramedTransport(new TSocket(SERVER_IP,SERVER_PORT));
            // 协议要和服务端一致
            TProtocol protocol = new TCompactProtocol(transport);
            Hello.Client client = new Hello.Client(
                    protocol);
            transport.open();
            String result = client.helloString(userName);
            System.out.println("Thrify client result =: " + result);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ThriftClientMain client = new ThriftClientMain();
//        client.startTserverClient("HelloCoder：醋酸菌");
//        client.startTNonblockingServerClient("HelloCoder：醋酸菌");
    }

}

package com.dubbo.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URI;

/**
 * @Author huangyongwen
 * @Date 2023/10/17 11:09
 * @Description
 **/
public class EndPointUtils {
    public static void main(String[] args) {

        String endpoint = "http://39.156.66.10:443/query";

        URI uri = URI.create(endpoint);

        System.out.println(uri.getHost() + ":" + uri.getPort());

        if (isSocketConnectAlive(uri.getHost(),uri.getPort())){
            System.out.println("active");
            int times =  callDeath(0);
        }

    }

    public static boolean isSocketConnectAlive(String hostname, int port) {
        boolean isAlive;
        SocketAddress socketAddress = new InetSocketAddress(hostname, port);
        // 超时设置，单位毫秒
        int timeout = 5000;
        try (Socket socket = new Socket()) {
            socket.connect(socketAddress, timeout);
            isAlive = true;
        } catch (IOException exception) {
            isAlive = false;
        }
        return isAlive;
    }

    public static int callDeath(int count) {
        return ++count;
    }
}

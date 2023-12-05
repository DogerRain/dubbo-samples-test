package com.hellocder.bio;

import lombok.SneakyThrows;

import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author huangyongwen
 * @Date 2023/12/4 10:00
 * @Description
 **/
public class BIOServer {
    @SneakyThrows
    public static void main(String[] args) {
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(7000);

        System.out.println("server start on " + serverSocket.getInetAddress().getHostAddress() + ":" + serverSocket.getLocalPort());

        while (true) {
            //同步阻塞，等到客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            //处理请求
            newCachedThreadPool.execute(new Runnable() {
                public void run() {//我们重写
                    //可以和客户端通讯
                    handler(socket);
                }
            });
        }

    }

    @SneakyThrows
    //编写一个handler方法，和客户端通讯
    public static void handler(Socket socket) {
        try {
            System.out.println("线程信息id = " + Thread.currentThread().getId() + ",名字 = " + Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //通过socket获取输入流
            InputStream inputStream = socket.getInputStream();
            //循环的读取客户端发送的数据
            while (true) {
//                System.out.println("线程信息id = " + Thread.currentThread().getId() + ",名字 = " + Thread.currentThread().getName());
//                System.out.println("read....");
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println(new String(bytes, 0, read));//输出客户端发送的数据
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和client的连接");
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

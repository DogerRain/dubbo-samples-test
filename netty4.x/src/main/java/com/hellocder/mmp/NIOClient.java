package com.hellocder.mmp;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author huangyongwen
 * @Date 2023/12/4 11:28
 * @Description
 **/
public class NIOClient {

    @SneakyThrows
    public static void main(String[] args) {


        SocketChannel socketChannel = SocketChannel.open();


        socketChannel.connect(new InetSocketAddress("localhost", 7001));

        String filename = "F:\\data\\log\\jarservice\\lastmile-client.log";

        //得到一个文件channel
        FileChannel fileChannel = new FileInputStream(filename).getChannel();
        //准备发送
        long startTime = System.currentTimeMillis();
        //在 linux 下一个 transferTo 方法就可以完成传输
        //在 windows 下一次调用 transferTo 只能发送 8m, 就需要分段传输文件,而且要主要
        //传输时的位置=》课后思考...
        //transferTo 底层使用到零拷贝
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送的总的字节数 = " + transferCount + " 耗时: " + (System.currentTimeMillis() - startTime));

        //关闭
        fileChannel.close();
    }
}

package com.hellocder.mmp;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Author huangyongwen
 * @Date 2023/12/4 11:36
 * @Description
 **/
public class NIOServer {
    public static void main(String[] args) throws Exception {
        InetSocketAddress address = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        //创建buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);

        String filename = "F:\\data\\log\\jarservice\\lastmile-client-copy.log";

        Path destinationPath = Paths.get(filename);
        //得到一个文件channel
        FileChannel destinationChannel = FileChannel.open(destinationPath,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readcount = 0;
            while (-1 != readcount) {
                try {
                    readcount = socketChannel.read(byteBuffer);
                    // 将 ByteBuffer 切换为读取模式
                    byteBuffer.flip();
                    //复制一个 ByteBuffer 。 其属性position、limit、capacity 和原来的一样
                    ByteBuffer duplicate = byteBuffer.duplicate();
                    System.out.println("server receive byteBuffer length" + readcount + ",value:" + StandardCharsets.UTF_8.decode(duplicate));
                    //把byteBuffer写到Channel
                    destinationChannel.write(byteBuffer);
                } catch (Exception ex) {
                    // ex.printStackTrace();
                    break;
                } finally {
                    // 清空 position 和 limit，准备下一次读
                    byteBuffer.clear();
                }
            }
            destinationChannel.close();
        }
    }
}

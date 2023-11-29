package com.hellocder.channel;

import lombok.SneakyThrows;

import java.lang.reflect.Array;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author HaC
 * @date 2023/11/30 0:40
 * @WebSite📶 :  https://learnjava.baimuxym.cn
 * @Description :
 * <p>
 * Scattering ：将数据写入到buffer时，可以采用buffer数组，依次写入
 * Gathering  ：从buffer读取数据时，可以采用buffer数组，依次读
 */
public class ScatteringAndGatheringTest {

    @SneakyThrows
    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定 端口到 socket
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组，
        // 假如是6个字节的字符大小，第一个ByteBuffer装不下，会顺延到第二个ByteBuffer
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

//        接收客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();

        //假设一个消息是 8 字节长度
        int msgLength = 8;

        while (true) {
            //
            int readByte = 0;

            while (readByte < msgLength) {
                //阻塞
                long l = socketChannel.read(byteBuffers);
//累计读取的字节数
                readByte += l;

                System.out.println("read bytes = " + readByte);

                Arrays.stream(byteBuffers).map(
                        byteBuffer -> "position=" + byteBuffer.position() + ", limit=" + byteBuffer.limit()).forEach(System.out::println
                );

                //让buffer变成可读状态
                Arrays.asList(byteBuffers).forEach(Buffer::flip);

                //读出来
                long writeByte = 0;
//                while (writeByte < msgLength) {
                    long wl = socketChannel.write(byteBuffers);
                    writeByte += wl;
//                }

//                resets this buffer's position
                Arrays.asList(byteBuffers).forEach(Buffer::clear);

                System.out.println("byteRead=" + readByte + ", writeByte=" + writeByte + ",messageLength=" + msgLength);

            }

        }

    }

}

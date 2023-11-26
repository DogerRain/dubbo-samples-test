package com.hellocder.channel;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author HaC
 * @date 2023/11/23 0:34
 * @WebSite📶 :  https://learnjava.baimuxym.cn
 * @Description
 */
public class NIOFileChannel {

    public static void main(String[] args) {
//        wirte();
//        read();
        copy();
    }

    @SneakyThrows
    private static void copy() {

        File file = new File("g:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();


        //        创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("g:\\file02.txt");
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();


        //        缓冲器 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);


        //未知文件大小
        while (true) {
//        清空标志位
            byteBuffer.clear();
            int read = fileInputStreamChannel.read(byteBuffer);

            System.out.println("read = " + read);

            //判定是否读完
            if (read == -1) {
                break;
            }

            byteBuffer.flip();
            //只要fileOutputStreamChannel未
            fileOutputStreamChannel.write(byteBuffer);

        }

        fileInputStream.close();
        fileOutputStream.close();
    }


    @SneakyThrows
    private static void wirte() {
        String str = "Hello HaC";

//        创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("g:\\file01.txt");

//
        FileChannel fileChannel = fileOutputStream.getChannel();

//        缓冲器 ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

//        把 字符放入
        byteBuffer.put(str.getBytes());

//
        byteBuffer.flip();

//       从 byteBuffer 写入数据到 Channel （把内存中的数据写入）
        fileChannel.write(byteBuffer);

        fileOutputStream.close();

    }

    @SneakyThrows
    private static void read() {
        File file = new File("g:\\file01.txt");

        FileInputStream fileInputStream = new FileInputStream(file);


        FileChannel fileChannel = fileInputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//      把 fileChannel的内容读到byteBuffer里面去
        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

        fileInputStream.close();

    }
}

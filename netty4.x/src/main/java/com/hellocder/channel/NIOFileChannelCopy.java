package com.hellocder.channel;

import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * @author HaC
 * @date 2023/11/29 0:21
 * @WebSite📶 :  https://learnjava.baimuxym.cn
 * @Description
 */
public class NIOFileChannelCopy {
    @SneakyThrows
    public static void main(String[] args) {
        FileInputStream fileInputStream = new FileInputStream("g:\\file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("g:\\file01_copy.txt");


        //从流中获取FileChannel
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();

        //拷贝
        fileOutputStreamChannel.transferFrom(fileInputStreamChannel, 0, fileInputStreamChannel.size());

        fileInputStreamChannel.close();
        fileOutputStreamChannel.close();
        fileInputStream.close();
        fileOutputStream.close();


    }
}

package com.hellocder.buffer;

import lombok.SneakyThrows;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author HaC
 * @date 2023/11/29 0:39
 * @WebSite📶 :  https://learnjava.baimuxym.cn
 * @Description 可以读取文件在内存中进行修改，操作系统不需要拷贝一次
 */
public class MappedByteBufferTets {

    @SneakyThrows
    public static void main(String[] args) {
        RandomAccessFile randomAccessFile = new RandomAccessFile("g:\\file02.txt", "rw");


        FileChannel fileChannel = randomAccessFile.getChannel();


        /**
         * 修改的范围就是 0~4 位置, 5 是指只映射 5个字节长度到内存，并不是位置 5
         */
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'x');
        mappedByteBuffer.put(1, (byte) 'g');
//        报错
        mappedByteBuffer.put(5, (byte) 'l');


//        Hello HaC  ——>  xgllo HaC



    }
}

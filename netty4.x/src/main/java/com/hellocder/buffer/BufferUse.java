package com.hellocder.buffer;

import java.nio.ByteBuffer;

/**
 * @author HaC
 * @date 2023/11/29 0:27
 * @WebSite📶 :  https://learnjava.baimuxym.cn
 * @Description
 */
public class BufferUse {
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(64);


        //put
        buffer.putInt(100);
        buffer.putChar('鱼');

        buffer.put("1hello world".getBytes());

        buffer.flip();


        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());

        byte[] array = buffer.array();

        System.out.println(new String(array));


    }
}

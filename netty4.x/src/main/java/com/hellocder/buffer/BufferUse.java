package com.hellocder.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

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
        buffer.putInt('a');
        buffer.putChar('s');

        buffer.put("hello world".getBytes());

        buffer.putInt(200);

        //切为可读模式
        buffer.flip();


        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());

        // 从 ByteBuffer 读取并转换为字符串 ， 后面 减去 int 共计 4个 字节
        byte[] byteArray = new byte[buffer.remaining() - 4];
        buffer.get(byteArray);
        String result = new String(byteArray, StandardCharsets.UTF_8);

        // 输出结果
        System.out.println(result);

        //后面还有4个字节还没读完
        System.out.println(buffer.remaining());



    }
}

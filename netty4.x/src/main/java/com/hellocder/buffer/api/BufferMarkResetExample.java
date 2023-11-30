package com.hellocder.buffer.api;

import java.nio.CharBuffer;

/**
 * @Author huangyongwen
 * @Date 2023/11/30 16:19
 * @Description
 **/
public class BufferMarkResetExample {
    public static void main(String[] args) {
        // 创建 CharBuffer ，相当于一个数据，position相当于是下标
        CharBuffer charBuffer = CharBuffer.allocate(8);

        // 添加元素到缓冲区
        charBuffer.put('A');
        charBuffer.put('B');
        charBuffer.put('C');

        // 打印当前位置
        System.out.println("Position before mark: " + charBuffer.position());

        // 调用 mark() 方法
        charBuffer.mark();

        // 添加更多元素
        charBuffer.put('D').put('E');

        // 打印当前位置
        System.out.println("Position after adding more elements: " + charBuffer.position());

        // 调用 reset() 方法，将position位置重置到 mark 的位置
        charBuffer.reset();

        // 打印重置后的位置
        System.out.println("Position after reset: " + charBuffer.position());

    }
}
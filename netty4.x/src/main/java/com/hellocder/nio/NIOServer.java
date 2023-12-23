package com.hellocder.nio;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author HaC
 * @date 2023/12/19 0:23
 * @WebSite📶 :  https://learnjava.baimuxym.cn
 * @Description
 */
public class NIOServer {
    @SneakyThrows
    public static void main(String[] args) {
        //1、创建 ServerSocketChannel ，相当于Socket
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        Selector selector = Selector.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

//        注册到 selector ，事件为 OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

//        等待客户端连接
        while (true) {
//            等待一秒，是否有事件发生
            if (selector.select(1000) == 0) {
                System.out.println("服务端等待了1秒，无连接");
                continue;
            }

//            selector.select 大于0表示获取到关注的事件，以下为事件的集合，通过 SelectionKey 获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();

                //对应 OP_ACCEPT 事件
                if (selectionKey.isAcceptable()) {
                    //表示有连接。为客户端生成一个 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
//                    注册到 selector ，事件为 OP_READ
//                    绑定一个buffer 到 Channel
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (selectionKey.isReadable()) {
                    //通过key获取channel
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
//                    通过 Channel获取已经关联的 bufer
                    ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                    channel.read(buffer);

                    System.out.println("from 客户端 ：" + new String(buffer.array()));

                }

            }
        }

    }
}

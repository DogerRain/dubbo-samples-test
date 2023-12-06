package com.hellocder.netty4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @Author huangyongwen
 * @Date 2023/11/1 15:55
 * @Description
 **/
public class SingleServer {
    public static void main(String[] args) {


        /**
         *
         * 创建 两个线程组。
         * 1、创建两个线程组 bossGroup 和 workerGroup
         * 2、 bossGroup 只是处理连接请求 , 真正的和客户端业务处理，会交给 workerGroup完成
         * 3、两个都是无限循环
         * 4、bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
         *
         *
         **/
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //   默认实际 cpu核数 * 2
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务端启动对象，配置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap
                    .group(bossGroup, workerGroup)  //设置两个线程组，分别是bossGroup、workerGroup
                    .channel(NioServerSocketChannel.class)  //使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //队列个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) //设置保持活动连接状态
                    //这里 handler 对应 bossGroup  ，  childHandler 对应 workerGroup
                    .handler(new SingleServer.MyParentHandler())
                    .childHandler(new MyClientInitializer())
            ;


            System.out.println(".....服务器 is ready...");
            ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
//  给 channelFuture 注册监听器，可以监听我们关心的事情
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (channelFuture.isSuccess()) {
                    System.out.println("监听端口 6668 成功");
                } else {
                    System.out.println("监听端口 6668 失败");
                }
            });
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


    public static class MyClientInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
//可以使用一个集合管理 SocketChannel， 再推送消息时，可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
            System.out.println("客户socketchannel hashcode=" + ch.hashCode());

            ChannelPipeline pipeline = ch.pipeline();

            //加入一个出站的handler 对数据进行一个编码
//            pipeline.addLast(new MyLongToByteEncoder());

            //加入一个自定义的handler ， 处理业务
            // 给我们的workerGroup 的 EventLoop 对应的管道设置处理器
            pipeline.addLast(new MyServerHandler());

            pipeline.addLast(new MyServerOutputHandler());


        }
    }


    public static class MyParentHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {

            System.out.println("MyParentHandler ， 读取...");
            // 传递给下一个处理器，很重要
            ctx.fireChannelRead(msg);
//            ctx.write(msg);

//            ctx.channel().eventLoop().execute();

        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            System.out.println("MyParentHandler handlerAdded");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            // 异常处理逻辑
            cause.printStackTrace();
            // 关闭连接
            ctx.close();
        }
    }


    /**
     * 说明
     * 1. 我们自定义一个Handler 需要继续netty 规定好的某个HandlerAdapter(规范)
     * 2. 这时我们自定义一个Handler , 才能称为一个handler
     */
    public static class MyServerHandler extends ChannelInboundHandlerAdapter {


        //其被channel在获取到数据的阶段进行调用，相当于接收 对方发送过来的数据

        /*
         * 1. ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel
         * 2. Object msg: 就是客户端发送的数据 默认Object
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {

            System.out.println("服务器读取线程 " + Thread.currentThread().getName() + " channel =" + ctx.channel());
//            System.out.println("server ctx =" + ctx);
//            System.out.println("看看Channel 和 ChannelPipeline的关系");
            Channel channel = ctx.channel();
//            ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接, 出站入站

            //将 msg 转成一个 ByteBuf
            //ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
            ByteBuf buf = (ByteBuf) msg;
            System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
            System.out.println("客户端地址:" + channel.remoteAddress());

            //
            ctx.fireChannelRead(msg);


        }

        //数据读取完毕
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            //writeAndFlush 是 write + flush
            //将数据写入到缓存，并刷新
            //一般讲，我们对这个发送的数据进行编码
            ctx.writeAndFlush(Unpooled.copiedBuffer("hello, 客户端~(>^ω^<)喵~", CharsetUtil.UTF_8));

        }

        //处理异常, 一般是需要关闭通道
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            System.out.println("handlerAdded");
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx) {
            System.out.println("channelRegistered");
        }

    }

    public static class MyServerOutputHandler extends ChannelOutboundHandlerAdapter {

        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            // 处理将数据写入到远端节点的逻辑
            // ...
            System.out.println("ChannelOutboundHandlerAdapter write");
            // 传递给下一个处理器
            ByteBuf data = (ByteBuf) msg;
            System.out.println("OutboundHandler2 write : " + data.toString(CharsetUtil.UTF_8));
            ctx.write(Unpooled.copiedBuffer(" add " + data.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8));
            ctx.flush();
        }

    }

}

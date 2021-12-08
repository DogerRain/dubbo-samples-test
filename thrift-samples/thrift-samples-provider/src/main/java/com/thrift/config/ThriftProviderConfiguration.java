package com.thrift.config;

import com.thrift.api.Hello;
import com.thrift.handler.MyTServerEventHandler;
import com.thrift.impl.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.TProcessor;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.transport.layered.TFramedTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.concurrent.*;

/**
 * @author huangyongwen
 * @date 2021/11/30
 * @Description
 */
@Configuration
@ComponentScan(value = {"com.thrift"})
@PropertySource("classpath:application-dev.properties")
@Slf4j
public class ThriftProviderConfiguration {

    @Value("${thrift.socket.port}")
    Integer port;



    /**
     * 1、
     *
     * TSimpleServer实现是非常的简单，循环监听新请求的到来并完成对请求的处理，是个单线程阻塞模型。
     * 由于是一次只能接收和处理一个socket连接，效率比较低，在实际开发过程中很少用到它。
     *
     * 注意客户端不能使用 非阻塞 传输同道（不能使用 TFramedTransport ）
     */
//        @Bean
    void TSimpleServerModel(){
        try {
            // 设置服务端口
            TServerSocket serverTransport = new TServerSocket(port);
            // 关联处理器与 Hello 服务的实现
            TProcessor processor = new Hello.Processor(new HelloServiceImpl());
            TSimpleServer.Args args = new TSimpleServer.Args(serverTransport);
//            args.processor(processor);
            args.processorFactory(new TProcessorFactory(processor));
            // 设置协议工厂为 TCompactProtocol.Factory
            args.protocolFactory(new TCompactProtocol.Factory());
            //设置服务模型
            TSimpleServer server = new TSimpleServer(args);
            server.setServerEventHandler(new MyTServerEventHandler());
            System.out.println("Thrift  TSimpleServer服务模型 provider Start on port " + port + "...");
            server.serve();
        } catch (TTransportException e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }

    /**
     *
     * 2、TThreadPoolServer 服务模型
     *
     *  ThreadPoolServer为解决了TSimpleServer不支持并发和多连接的问题, 引入了线程池。但仍然是多线程阻塞模式即实现的模型是One Thread Per Connection。
     *  线程池采用能线程数可伸缩的模式，线程池中的队列采用同步队列(SynchronousQueue)。
     *
     *    TThreadPoolServer模式优点：
     *
     *        线程池模式中，数据读取和业务处理都交由线程池完成，主线程只负责监听新连接，因此在并发量较大时新连接也能够被及时接受。
     *        线程池模式比较适合服务器端能预知最多有多少个客户端并发的情况，这时每个请求都能被业务线程池及时处理，性能也非常高。
     */
//    @Bean
    public void TThreadPoolServerModel () {
        try {
            System.out.println("HelloWorld TThreadPoolServer start ....");

            TProcessor tprocessor = new Hello.Processor<Hello.Iface>(
                    new HelloServiceImpl());

            TServerSocket serverTransport = new TServerSocket(port);
            TThreadPoolServer.Args args = new TThreadPoolServer.Args(
                    serverTransport);
            args.processor(tprocessor);
            args.protocolFactory(new TCompactProtocol.Factory());
            // 线程池服务模型，使用标准的阻塞式IO，预先创建一组线程处理请求。
            // 多个线程，主要负责客户端的IO处理
            args.minWorkerThreads = 16;
            // 工作线程池
            ExecutorService executorService =  new ThreadPoolExecutor(100, 500,
                    60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
            args.executorService(executorService);
            TServer server = new TThreadPoolServer(args);
            System.out.println("Thrift  TThreadPool模型 provider Start on port " + port + "...");
            server.serve();

        } catch (Exception e) {
            System.out.println("Server start error!!!");
            e.printStackTrace();
        }
    }


    /**
     * 3、
     * TNonblockingServer采用单线程非阻塞(NIO)的模式, 借助Channel/Selector机制, 采用IO事件模型来处理。
     * 所有的socket都被注册到selector中，在一个线程中通过seletor循环监控所有的socket，
     * 每次selector结束时，处理所有的处于就绪状态的socket，对于有数据到来的socket进行数据读取操作，
     * 对于有数据发送的socket则进行数据发送，对于监听socket则产生一个新业务socket并将其注册到selector中。
     */
//    @Bean
    void TNonblockingServerTransportModel(){
        try {
            // 非阻塞式的，配合TFramedTransport使用
            TNonblockingServerTransport serverTransport = new TNonblockingServerSocket(port);
            // 关联处理器与Service服务的实现
            TProcessor processor = new Hello.Processor<Hello.Iface>(new HelloServiceImpl());

            TNonblockingServer.Args args = new TNonblockingServer.Args(serverTransport);
            // 设置协议工厂，高效率的、密集的二进制编码格式进行数据传输协议
            args.protocolFactory(new TCompactProtocol.Factory());
            // 设置传输工厂，使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO
            args.transportFactory(new TFramedTransport.Factory());
            args.processorFactory(new TProcessorFactory(processor));
            TNonblockingServer server = new TNonblockingServer(args);
            System.out.println("Thrift  TNonblockingServerTransportModel模型 provider Start on port " + port + "...");
            server.serve();
        } catch (TTransportException e) {
            log.error("Server start error!!!",e);
        }
    }

    /**
     * 4、
     * THsHaServer
     * THsHaServer类是TNonblockingServer类的子类，为解决TNonblockingServer的缺点, THsHa引入了线程池去处理, 其模型把读写任务放到线程池去处理即多线程非阻塞模式
     */
//    @Bean
    void THsHaServerModel(){
        try {
            TNonblockingServerSocket socket = new TNonblockingServerSocket(port);
            TProcessor processor = new Hello.Processor<Hello.Iface>(new HelloServiceImpl());
            THsHaServer.Args args = new THsHaServer.Args(socket);

            // 设置协议工厂，高效率的、密集的二进制编码格式进行数据传输协议
            args.protocolFactory(new TCompactProtocol.Factory());
            // 设置传输工厂，使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO,服务端和客户端都需要指定数据传输方式为TFramedTransport
            args.transportFactory(new TFramedTransport.Factory());
            // 设置处理器工厂,只返回一个单例实例
            args.processorFactory(new TProcessorFactory(processor));
//            args.processor(processor);
            THsHaServer server = new THsHaServer(args);
            System.out.println("Thrift  THsHaServer服务模型 provider Start on port " + port + "...");
            server.serve();
        } catch (TTransportException e) {
            log.error("Server start error!!!",e);
        }
    }


    /**
     *
     * 5、
     *
     *   TThreadedSelectorServer是大家广泛采用的服务模型，其多线程服务器端使用非堵塞式I/O模型，
     *   是对TNonblockingServer的扩充, 其分离了Accept和Read/Write的Selector线程, 同时引入Worker工作线程池。
     *
     */
        @Bean
    void TThreadedSelectorServerModel(){
        try {
            TNonblockingServerSocket socket = new TNonblockingServerSocket(port);
            TProcessor processor = new Hello.Processor<Hello.Iface>(new HelloServiceImpl());
            // 目前Thrift提供的最高级的模式，可并发处理客户端请求,多线程半同步半异步的服务模型
            TThreadedSelectorServer.Args args = new TThreadedSelectorServer.Args(socket);

            args.selectorThreads(200);
            args.workerThreads(500);
            // 工作线程池
//            ExecutorService executorService =  new ThreadPoolExecutor(500, 1000,
//                    60, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
//            args.executorService(executorService);

            // 设置协议工厂，高效率的、密集的二进制编码格式进行数据传输协议
            args.protocolFactory(new TCompactProtocol.Factory());
            // 设置传输工厂，使用非阻塞方式，按块的大小进行传输，类似于Java中的NIO,服务端和客户端都需要指定数据传输方式为TFramedTransport
            args.transportFactory(new TFramedTransport.Factory());
            // 设置处理器工厂,只返回一个单例实例
            args.processorFactory(new TProcessorFactory(processor));
//            args.processor(processor);
            TThreadedSelectorServer server = new TThreadedSelectorServer(args);
            System.out.println("Thrift  TThreadedSelectorServer服务模型 provider Start on port " + port + "...");
            server.serve();
        } catch (TTransportException e) {
            log.error("Server start error!!!",e);
        }
    }

}

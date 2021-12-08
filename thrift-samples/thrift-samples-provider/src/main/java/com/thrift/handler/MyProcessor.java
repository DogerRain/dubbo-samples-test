package com.thrift.handler;

import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;

/**
 * @author huangyongwen
 * @date 2021/12/8
 * @Description
 *
 *
 *  自定义 Processor，实现 TProcessor 接口
 *
 *  不建议使用这种方式，建议使用  {@link MyTServerEventHandler }
 *
 */
public class MyProcessor implements TProcessor {

    private TProcessor processor;

    public MyProcessor(TProcessor processor) {
        this.processor = processor;
    }

    /**
     * 该方法，客户端每调用一次，就会触发一次
     * <p>
     * //    0.9版本可能不一样，需要返回一个 布尔值
     */
    @Override
    public void process(TProtocol in, TProtocol out) throws TException {
        /**
         * 从TProtocol里面获取TTransport对象
         * 把TTransport对象转换成TSocket，然后在TSocket里面获取Socket，就可以拿到客户端IP
         */
        TSocket socket = (TSocket) in.getTransport();
        System.out.println("requests from --->>>" + socket.getSocket().getRemoteSocketAddress()
                + ", port --->>> " + socket.getSocket().getPort()
        );
        processor.process(in, out);
    }
}

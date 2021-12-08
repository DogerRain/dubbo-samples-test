package com.thrift.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.ServerContext;
import org.apache.thrift.server.TServerEventHandler;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

/**
 * @author huangyongwen
 * @date 2021/12/8
 * @Description
 */
@Slf4j
public class MyTServerEventHandler implements TServerEventHandler {
    /**
     * 服务成功启动后执行
     */
    @Override
    public void preServe() {
        log.info("Thrift Server 启动");
    }

    /**
     * 创建Context的时候，触发
     * 在server启动后，只会执行一次
     */
    @Override
    public ServerContext createContext(TProtocol input, TProtocol output) {
        return null;
    }

    /**
     * 删除Context的时候，触发
     * 在server启动后，只会执行一次
     */
    @Override
    public void deleteContext(ServerContext serverContext, TProtocol input, TProtocol output) {

    }

    /**
     * 调用RPC服务的时候触发
     * 每调用一次方法，就会触发一次
     */
    @Override
    public void processContext(ServerContext serverContext, TTransport inputTransport, TTransport outputTransport) {
        /**
         * 把TTransport对象转换成TSocket，然后在TSocket里面获取Socket，就可以拿到客户端IP
         */
        try {
            /**
             * 如果你用了 TFramedTransport，则会报错
             *  cannot be cast to org.apache.thrift.transport.TSocket
             * 暂时无解，辣鸡，官网也没有说明，艹
             */
            TSocket socket = (TSocket) inputTransport;
            System.out.println("requests from --->>>" + socket.getSocket().getRemoteSocketAddress()
                    + ", port --->>> " + socket.getSocket().getPort()
            );
        }catch (Exception e){
            System.out.println("转换错误");
        }

        System.out.println("method invoke ... ");

    }
}

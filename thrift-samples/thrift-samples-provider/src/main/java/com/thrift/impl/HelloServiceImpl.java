package com.thrift.impl;

import com.shrift.api.Hello;
import org.apache.thrift.TException;

/**
 * @author huangyongwen
 * @date 2021/11/24
 * @Description
 */
public class HelloServiceImpl implements Hello.Iface{
    @Override
    public boolean helloBoolean(boolean para) throws TException {
        return para;
    }
    @Override
    public int helloInt(int para) throws TException {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return para;
    }
    @Override
    public String helloNull() throws TException {
        return null;
    }
    @Override
    public String helloString(String para) throws TException {
        System.out.println("入参字节大小：" + para.length());
        return para;
    }
    @Override
    public void helloVoid() throws TException {
        System.out.println("Hello World");
    }
}

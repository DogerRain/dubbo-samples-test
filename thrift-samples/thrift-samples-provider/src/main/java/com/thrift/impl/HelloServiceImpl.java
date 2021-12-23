package com.thrift.impl;

import com.thrift.api.Hello;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.Random;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/24
 * @Description
 */
@Slf4j
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
//        log.info("request from consumer, parameterSize:{}",para.length());
        System.out.println("request from consumer, parameterSize:"+para.length());
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        doWhileAdd();
        return para;
    }
    @Override
    public void helloVoid() throws TException {
        System.out.println("Hello World");
    }

    void doWhileAdd(){
        Random random = new Random(1);
        int result =0;
        int result2=0;
        for (int i = 1;i<=10000;i++){
            int num = random.nextInt(1000);
            result +=num;
            result2 -=num;
        }
    }


    public static void main(String[] args) {
        new HelloServiceImpl().doWhileAdd();
    }
}

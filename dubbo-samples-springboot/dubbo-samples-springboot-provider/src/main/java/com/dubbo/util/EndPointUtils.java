package com.dubbo.util;

import java.net.URI;

/**
 * @Author huangyongwen
 * @Date 2023/10/17 11:09
 * @Description
 **/
public class EndPointUtils {
    public static void main(String[] args) {

        String endpoint = "http://10.131.1.98:4317";

        URI uri = URI.create(endpoint);

        System.out.println(uri.getHost());


        System.out.println(callDeath(0));

    }

    public static int callDeath(int count) {
        return ++count;
    }
}

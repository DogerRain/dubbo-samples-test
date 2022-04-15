package com.hac.log4j;

import org.apache.logging.log4j.LogManager;

public class Log4jTolog4j2 {

    //log4j
    private static final org.apache.log4j.Logger log4j = org.apache.log4j.Logger.getLogger(Log4jTolog4j2.class.getName());

    //log4j2
    private static final org.apache.logging.log4j.Logger log4j2 = LogManager.getLogger(Log4jTolog4j2.class.getName());


    public static void main(String[] args) {

        //log4j.xml 的配置
        log4j.info("log4j:" + "info");
        //log4j2.xml 的配置
        log4j2.info("log4j2:", "info");

    }

}

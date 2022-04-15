package com.hac.sl4jSimple;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sl4jSimple {
    private static final Logger logger = LoggerFactory.getLogger(Sl4jSimple.class);

    public static void main(String[] args) {
        logger.info("Sl4jSimple:{}","info");
        logger.debug("Sl4jSimple:{}","debug");
    }
}

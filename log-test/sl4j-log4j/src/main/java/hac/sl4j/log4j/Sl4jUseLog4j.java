package hac.sl4j.log4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sl4jUseLog4j {


    // sl4j
    private static final Logger sl4jLogger = LoggerFactory.getLogger(Sl4jUseLog4j.class);


    public static void main(String[] args) {

        sl4jLogger.info("sl4jLogger:{}", "info");

    }

}

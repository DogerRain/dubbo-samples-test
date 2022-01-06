package log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//import org.slf4j.Logger;

/**
 * @author huangyongwen
 * @date 2022/1/4
 * @Description
 */
public class Log4j2 {


//  log4j + Sl4j
    private static final Logger log = LoggerFactory.getLogger(Log4j2.class);

    public static void main(String[] args) {

        log.info("SL4j:{}","info");

    }
}

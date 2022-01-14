package log;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author huangyongwen
 * @date 2022/1/6
 * @Description 直接使用 log4j2
 */

public class Log4j2API {

    private static Logger log = LogManager.getLogger(Log4j2API.class.getName());

    public static void main(String[] args) {

        log.info("直接使用log4j2");

    }

}

package log;

import org.apache.log4j.Logger;

/**
 * @author huangyongwen
 * @date 2022/1/6
 * @Description 直接使用 log4j
 */

public class Log4jAPI {

    private static final Logger log = Logger.getLogger(Log4jAPI.class.getName());

    public static void main(String[] args) {

        log.info("直接使用log4j");

    }

}

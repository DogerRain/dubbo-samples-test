package log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sl4j.LogSl4jAPI;

/**
 * @author huangyongwen
 * @date 2022/1/12
 * @Description
 */
public class LogSl4jAPIImpl {

    private static final Logger log = LoggerFactory.getLogger(LogSl4jAPIImpl.class);

    public static void main(String[] ars) {
        LogSl4jAPI logSl4jAPI = new LogSl4jAPI();
        logSl4jAPI.main(null);

        log.info("SL4j 结合");

    }
}

package log;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hac.Log4jCoexistLog4j2;

/**
 * @author huangyongwen
 * @date 2022/1/12
 * @Description
 */
public class Log4jCoexistLog4J2ImplTest {

    private static final Logger log = LoggerFactory.getLogger(Log4jCoexistLog4J2ImplTest.class);

    @Test
    public void TestLog() {
        Log4jCoexistLog4j2 logSl4jAPI = new Log4jCoexistLog4j2();
        logSl4jAPI.main(null);

        log.info("SL4j 结合");

    }
}

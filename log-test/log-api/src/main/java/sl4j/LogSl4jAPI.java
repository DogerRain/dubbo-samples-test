package sl4j;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangyongwen
 * @date 2022/1/10
 * @Description
 */
public class LogSl4jAPI {

    private static final Logger log = LoggerFactory.getLogger(LogSl4jAPI.class);


    public static void main(String[] args) {

        log.info("log-api项目:{}","info");

//        System.out.println("Hello Sl4j");

    }
}

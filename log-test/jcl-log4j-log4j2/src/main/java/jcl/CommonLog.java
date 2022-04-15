package jcl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.logging.Logger;


/**
 * @author huangyongwen
 * @date 2022/1/4
 * @Description
 */
public class CommonLog {
//jdk 自带的 jul 日志
    private static final Logger jul = Logger.getLogger(CommonLog.class.getName());

    /**
     * common-logging 类似于 sl4j ，也是个日志门面
     *     默认使用log4j，所以如果你依赖包出现 log4j，会自动寻找 log4j.xml ；如果出现了log4j2，会自动寻找log4j2.xml；
     *     否则会使用原生JUL实现。
     */

    private static final Log log = LogFactory.getLog(CommonLog.class);

    public static void main(String[] args) {
        log.info("commons-logging:info");

        jul.info("java.util.logging,jul");
    }
}

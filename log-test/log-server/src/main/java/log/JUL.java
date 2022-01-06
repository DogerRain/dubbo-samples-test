package log;


import java.util.logging.Logger;


/**
 * @author huangyongwen
 * @date 2022/1/4
 * @Description
 */
public class JUL {

    //JUC jdk原生日志
    private static final Logger log = Logger.getLogger(JUL.class.getName());


    public static void main(String[] args) {

        log.info("JUL" + "info");


    }
}

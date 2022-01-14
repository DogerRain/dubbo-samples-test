package log;


import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author huangyongwen
 * @date 2022/1/4
 * @Description
 */
public class Log4j {


//  log4j + Sl4j
    private static final Logger log = LoggerFactory.getLogger(Log4j.class);

    private static ZooKeeper zkClient;

    public static void main(String[] args) throws IOException {
        init();
// 默认是 log4j
        log.info("SL4j:{}","info");

//以当前项目的日志配置文件为准，缺失 以 api 项目 日志配置为准
        Log4jAPI log4jAPI = new Log4jAPI();
        log4jAPI.main(null);
//以当前项目日志配置文件为准， 缺失 以 api 项目 日志配置为准
        Log4j2API log4j2API = new Log4j2API();
        log4j2API.main(null);

    }

    public static void init() throws IOException {
        zkClient = new ZooKeeper("127.0.0.1:2181", 2000, new Watcher() {
            // 监听器，如果开启
            @Override
            public void process(WatchedEvent watchedEvent) {
                String path = watchedEvent.getPath();
                System.out.println("---------start----------");
                List<String> children;
                try {
                    children = zkClient.getChildren("/", true);

                    for (String child : children) {
                        System.out.println(child);
                    }
                    System.out.println("---------end----------");
                } catch (KeeperException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

    }
}

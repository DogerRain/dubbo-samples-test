package com.dubbo.api;

import com.dubbo.vo.User;

import java.util.List;

/**
 * @author 醋酸菌HaC | WebSite📶 : https://rain.baimuxym.cn
 * @site
 * @date 2021/11/18
 * @Description 压测接口
 */
public interface StressTestService {

    String StressString(String string);

    String StressString1k(String string);

    String StressString1kNoFile(String string);

    User StressTestPojo(User user);

    List<User> StressListUser(List<User> list);

    String StressTest50K(String bytes);

    String StressTest100K(String bytes);

}

package com.dubbo.api;

/**
 * @author huangyongwen
 * @date 2021/11/18
 * @Description 压测接口
 */
public interface StressTestService {

    String StressTest50K(byte[] bytes);

    String StressTest100K(byte[] bytes);

}

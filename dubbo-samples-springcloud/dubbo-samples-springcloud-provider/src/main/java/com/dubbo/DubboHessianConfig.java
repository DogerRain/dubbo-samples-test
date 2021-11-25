package com.dubbo;

import org.apache.dubbo.config.ProtocolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huangyongwen
 * @date 2021/11/25
 * @Description
 */
@Configuration
public class DubboHessianConfig {

    // 配置rest协议
    @Bean("hessian")
    public ProtocolConfig restProtocolConfig() {
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("hessian");
        protocolConfig.setId("hessian");
        protocolConfig.setServer("tomcat");
        protocolConfig.setPort(8090);
        protocolConfig.setAccepts(500);
        protocolConfig.setThreads(100);
        // 可继续增加其它配置
        return protocolConfig;
    }
}


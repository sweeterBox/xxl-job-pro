package com.xxl.job.admin.instance;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sweeter
 * @date 2023/1/14
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnCloudEnabled
public class DiscoveryClientConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public DiscoveryInstance discoveryInstance() {
        return new CloudDiscoveryInstance();
    }
}

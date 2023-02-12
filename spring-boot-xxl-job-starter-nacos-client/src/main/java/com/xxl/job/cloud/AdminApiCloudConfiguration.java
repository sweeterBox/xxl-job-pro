package com.xxl.job.cloud;

import com.xxl.job.client.executor.client.AdminApiClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author sweeter
 * @date 2023/1/15
 */
@ConditionalOnCloudEnabled
@Configuration(proxyBeanMethods = false)
public class AdminApiCloudConfiguration {


    @ConditionalOnMissingBean
    @LoadBalanced
    @Bean
    public RestTemplate loadBalancedRestTemplate() {
        return new RestTemplate();
    }

    @ConditionalOnMissingBean
    @Bean
    public AdminApiClient adminApiClient() {
        return new AdminApiLoadBalancedClient();
    }


}

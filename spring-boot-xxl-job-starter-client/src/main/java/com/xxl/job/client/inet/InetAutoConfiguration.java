package com.xxl.job.client.inet;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sweeter
 * @date 2023/1/15
 */
/*@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(value = "spring.boot.xxl.job.inet.enabled", matchIfMissing = true)
@AutoConfigureOrder(0)
@EnableConfigurationProperties*/
public class InetAutoConfiguration {

    @Bean
    public InetProperties inetProperties() {
        return new InetProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    public InetUtils inetUtils(InetProperties inetProperties) {
        return new InetUtils(inetProperties);
    }
}

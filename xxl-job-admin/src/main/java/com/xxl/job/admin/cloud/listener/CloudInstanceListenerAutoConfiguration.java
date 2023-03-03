/*
package com.xxl.job.admin.cloud.listener;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.util.List;

*/
/**
 * @author sweeter
 * @date 2023/1/14
 *//*

@Configuration(proxyBeanMethods = false)
public class CloudInstanceListenerAutoConfiguration {

    @Configuration(proxyBeanMethods = false)
    public static class NacosUpAndDownListenerConfiguration {

        @ConditionalOnMissingBean
        @Primary
        @Bean
        public CompositeInstanceUpAndDownListener compositeInstanceUpAndDownListener(List<InstanceUpAndDownListener> allUpAndDownListener) {
            return new CompositeInstanceUpAndDownListener(allUpAndDownListener);
        }

        @ConditionalOnMissingBean
        @Bean
        public InstancesChangeEventListener instancesChangeEventListener() {
            return new InstancesChangeEventListener();
        }

    }


    @AutoConfigureBefore(CompositeInstanceUpAndDownListener.class)
    @Configuration(proxyBeanMethods = false)
    public static class XxlJobClientUpAndDownListenerConfiguration {

        @ConditionalOnMissingBean
        @Bean
        public XxlJobAdminUpAndDownListener xxlJobClientUpAndDownListener() {
            return new XxlJobAdminUpAndDownListener();
        }
    }
}
*/

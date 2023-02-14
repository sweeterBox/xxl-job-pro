package com.xxl.job.admin.notify;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

/**
 * @author sweeter
 * @date 2023/2/13
 */
@Configuration(proxyBeanMethods = false)
public class NotifierAutoConfiguration {

    @ConditionalOnMissingBean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = "xxl.job.notify.feishu", name = "webhook-url")
    @AutoConfigureBefore({RestTemplate.class})
    @Lazy(false)
    public static class FeiShuNotifierConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConfigurationProperties("xxl.job.notify.feishu")
        public FeiShuNotifier feiShuNotifier(RestTemplate restTemplate) {
            return new FeiShuNotifier(restTemplate);
        }

    }


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(prefix = "xxl.job.notify.webhook", name = "webhook-url")
    @AutoConfigureBefore({RestTemplate.class})
    @Lazy(false)
    public static class WebhookNotifierConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConfigurationProperties("xxl.job.notify.webhook")
        public WebhookNotifier webhookNotifier(RestTemplate restTemplate) {
            return new WebhookNotifier(restTemplate);
        }

    }


    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean(MailSender.class)
    @Lazy(false)
    public static class MailNotifierConfiguration {

        @Bean
        @ConditionalOnMissingBean
        @ConfigurationProperties("xxl.job.notify.mail")
        public MailNotifier mailNotifier(JavaMailSender mailSender) {
            return new MailNotifier(mailSender);
        }

    }
}

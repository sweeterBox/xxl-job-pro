package com.xxl.job.client;

import com.xxl.job.client.executor.Executor;
import com.xxl.job.client.executor.XxlJobExecutor;
import com.xxl.job.client.executor.XxlJobSpringExecutor;
import com.xxl.job.client.executor.client.AdminApiHttpClient;
import com.xxl.job.client.executor.impl.DefaultExecutor;
import com.xxl.job.client.repository.ScheduledHandlerRepository;
import com.xxl.job.client.repository.ScheduledThreadRepository;
import com.xxl.job.client.repository.SimpleScheduledHandlerRepository;
import com.xxl.job.client.repository.SimpleScheduledThreadRepository;
import com.xxl.job.client.single.SingleConfiguration;
import com.xxl.job.client.utils.IpUtil;
import com.xxl.job.client.utils.PortUtils;
import org.apache.commons.lang3.StringUtils;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Objects;

/**
 * @author sweeter
 * @date 2022/11/3
 */
@EnableConfigurationProperties(XxlJobProperties.class)
@Configuration
public class XxlJobClientAutoConfiguration {

    /**
     * @author sweeter
     * @date 2023/2/25
     */
    public static class ProxyXxlJobClientConditional implements Condition {

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            String enabled = conditionContext.getEnvironment().getProperty("spring.boot.xxl.job.client.enabled", "false");
            String proxyEnabled = conditionContext.getEnvironment().getProperty("spring.boot.xxl.job.client.proxyEnabled", "false");
            return Boolean.parseBoolean(enabled) && Boolean.parseBoolean(proxyEnabled);
        }
    }

    /**
     * @author sweeter
     * @date 2023/2/25
     */
    public static class SimpleXxlJobClientConditional implements Condition {

        @Override
        public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
            String enabled = conditionContext.getEnvironment().getProperty("spring.boot.xxl.job.client.enabled", "false");
            String proxyEnabled = conditionContext.getEnvironment().getProperty("spring.boot.xxl.job.client.proxyEnabled", "false");
            return Boolean.parseBoolean(enabled) && !Boolean.parseBoolean(proxyEnabled);
        }
    }

    @Conditional(SimpleXxlJobClientConditional.class)
    @Configuration
    public static class SimpleXxlJobClientConfiguration {

        private final XxlJobProperties xxlJobProperties;

        public SimpleXxlJobClientConfiguration(XxlJobProperties xxlJobProperties) {
            this.xxlJobProperties = xxlJobProperties;
        }
        @ConditionalOnMissingBean
        @Bean
        public ScheduledHandlerRepository jobHandlerRepository() {
            return new SimpleScheduledHandlerRepository();
        }

        @ConditionalOnMissingBean
        @Bean
        public ScheduledThreadRepository jobThreadRepository() {
            return new SimpleScheduledThreadRepository();
        }

        @ConditionalOnMissingBean
        @Bean
        public Executor executor(ScheduledHandlerRepository jobHandlerRepository, ScheduledThreadRepository jobThreadRepository) {
            return new DefaultExecutor(jobHandlerRepository, jobThreadRepository);
        }

        @ConditionalOnMissingBean
        @Bean
        public XxlJobExecutor xxlJobExecutor(ScheduledHandlerRepository jobHandlerRepository, ScheduledThreadRepository jobThreadRepository, Executor executor) {
            XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
            //TODO 需要支持多admin
            if (Objects.nonNull(this.xxlJobProperties.getAdminAddresses()) && this.xxlJobProperties.getAdminAddresses().length > 0) {
                xxlJobSpringExecutor.setAdminAddresses(this.xxlJobProperties.getAdminAddresses()[0]);
            }else {
                //
            }
            xxlJobSpringExecutor.setName(xxlJobProperties.getName());
            xxlJobSpringExecutor.setTitle(xxlJobProperties.getTitle());
            if (StringUtils.isBlank(xxlJobProperties.getIp())) {
                String ip = IpUtil.getIp();
                xxlJobSpringExecutor.setIp(ip);
                xxlJobProperties.setIp(ip);
            }else {
                xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
            }
            if (Objects.isNull(xxlJobProperties.getPort())) {
                int port = PortUtils.findAvailablePort(8283);
                xxlJobSpringExecutor.setPort(port);
                xxlJobProperties.setPort(port);
            }else {
                xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
            }
            xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
            xxlJobSpringExecutor.setClientUrl(xxlJobProperties.getAddress());
            xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
            xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
            xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
            xxlJobSpringExecutor.setJobHandlerRepository(jobHandlerRepository);
            xxlJobSpringExecutor.setJobThreadRepository(jobThreadRepository);
            xxlJobSpringExecutor.setExecutor(executor);
            return xxlJobSpringExecutor;
        }

        @ConditionalOnMissingBean
        @Bean
        public AdminApiHttpClient adminApiHttpClient() {
            return new AdminApiHttpClient(this.xxlJobProperties.getAdminAddresses()[0], this.xxlJobProperties.getAccessToken());
        }

        @ConditionalOnMissingBean
        @Bean
        public SingleConfiguration singleInit() {
            return new SingleConfiguration(this.xxlJobProperties);
        }


    }


    @Conditional(ProxyXxlJobClientConditional.class)
    @Configuration
    public static class ProxyXxlJobClientConfiguration {

        private final XxlJobProperties xxlJobProperties;

        public ProxyXxlJobClientConfiguration(XxlJobProperties xxlJobProperties) {
            this.xxlJobProperties = xxlJobProperties;
        }

        @ConditionalOnMissingBean
        @Bean
        public ScheduledHandlerRepository jobHandlerRepository() {
            return new SimpleScheduledHandlerRepository();
        }

        @ConditionalOnMissingBean
        @Bean
        public ScheduledThreadRepository jobThreadRepository() {
            return new SimpleScheduledThreadRepository();
        }

        @ConditionalOnMissingBean
        @Bean
        public Executor executor(ScheduledHandlerRepository jobHandlerRepository, ScheduledThreadRepository jobThreadRepository) {
            return new DefaultExecutor(jobHandlerRepository, jobThreadRepository);
        }

        @Order(Ordered.HIGHEST_PRECEDENCE)
        @ConditionalOnMissingBean
        @Bean(destroyMethod = "destroy")
        public XxlJobExecutor xxlJobExecutor(ScheduledHandlerRepository jobHandlerRepository
                , ScheduledThreadRepository jobThreadRepository, Executor executor, ProxyProperties proxyProperties) {
            XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
            //TODO 需要支持多admin
            xxlJobSpringExecutor.setAdminAddresses(this.xxlJobProperties.getAddress());
            xxlJobSpringExecutor.setName(xxlJobProperties.getName());
            xxlJobSpringExecutor.setTitle(xxlJobProperties.getTitle());
            xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
            xxlJobSpringExecutor.setPort(xxlJobProperties.getPort());
            xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
            xxlJobSpringExecutor.setLogPath(xxlJobProperties.getLogPath());
            xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperties.getLogRetentionDays());
            xxlJobSpringExecutor.setJobHandlerRepository(jobHandlerRepository);
            xxlJobSpringExecutor.setJobThreadRepository(jobThreadRepository);
            xxlJobSpringExecutor.setExecutor(executor);
            String clientUrl = UriComponentsBuilder.fromUri(proxyProperties.getUri())
                    .port(xxlJobProperties.getPort())
                    .path(xxlJobProperties.getContextPath())
                    .path("/")
                    .build()
                    .toUriString();
            xxlJobSpringExecutor.setClientUrl(clientUrl);
            xxlJobSpringExecutor.setIp(proxyProperties.getIp());
            xxlJobSpringExecutor.setPort(proxyProperties.getPort());
            return xxlJobSpringExecutor;
        }

        @ConditionalOnMissingBean
        @Bean
        public ServletRegistrationBean<ProxyServlet> servletRegistrationBean(ProxyProperties proxyProperties){
            String proxyMapping = xxlJobProperties.getContextPath() + "/*";
            String proxyClientUrl = UriComponentsBuilder.fromUri(proxyProperties.getUri()).build().toUriString();
            ServletRegistrationBean<ProxyServlet> servletRegistrationBean = new ServletRegistrationBean<>(new ProxyServlet(), proxyMapping);
            servletRegistrationBean.addInitParameter("targetUri", proxyClientUrl);
            servletRegistrationBean.addInitParameter(ProxyServlet.P_LOG, "true");
            return servletRegistrationBean;
        }

        @Bean
        public ProxyProperties proxyProperties() {
            int port = PortUtils.findAvailablePort(8283);
            String ip ="127.0.0.1";
            URI uri = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host(ip)
                    .port(port)
                    .build()
                    .toUri();
            return new ProxyProperties(port, ip, uri);
        }


        public static class ProxyProperties {

            private int port;

            private String ip;

            private URI uri;

            public ProxyProperties(int port, String ip, URI uri) {
                this.port = port;
                this.ip = ip;
                this.uri = uri;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public URI getUri() {
                return uri;
            }

            public void setUri(URI uri) {
                this.uri = uri;
            }
        }

    }



}

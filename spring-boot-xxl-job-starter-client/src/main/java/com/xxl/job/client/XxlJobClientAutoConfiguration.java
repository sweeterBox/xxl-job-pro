package com.xxl.job.client;

import com.xxl.job.client.executor.Executor;
import com.xxl.job.client.executor.XxlJobExecutor;
import com.xxl.job.client.executor.XxlJobSpringExecutor;
import com.xxl.job.client.executor.impl.DefaultExecutor;
import com.xxl.job.client.repository.JobHandlerRepository;
import com.xxl.job.client.repository.JobThreadRepository;
import com.xxl.job.client.repository.SimpleJobHandlerRepository;
import com.xxl.job.client.repository.SimpleJobThreadRepository;
import com.xxl.job.client.utils.PortUtils;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

/**
 * @author sweeter
 * @date 2022/11/3
 */
@Configuration
public class XxlJobClientAutoConfiguration {

    //@Configuration
   // @ConditionalOnExpression("${spring.boot.xxljob.enabled:false} && !${spring.boot.xxljob.proxyEnabled:false}")
    public static class SimpleXxlJobClientConfiguration {

        private final XxlJobProperties xxlJobProperties;

        public SimpleXxlJobClientConfiguration(XxlJobProperties xxlJobProperties) {
            this.xxlJobProperties = xxlJobProperties;
        }
        @ConditionalOnMissingBean
        @Bean
        public JobHandlerRepository jobHandlerRepository() {
            return new SimpleJobHandlerRepository();
        }

        @ConditionalOnMissingBean
        @Bean
        public JobThreadRepository jobThreadRepository() {
            return new SimpleJobThreadRepository();
        }

        @ConditionalOnMissingBean
        @Bean
        public Executor executor(JobHandlerRepository jobHandlerRepository,JobThreadRepository jobThreadRepository) {
            return new DefaultExecutor(jobHandlerRepository, jobThreadRepository);
        }

        @ConditionalOnMissingBean
        @Bean
        public XxlJobExecutor xxlJobExecutor(JobHandlerRepository jobHandlerRepository, JobThreadRepository jobThreadRepository, Executor executor) {
            XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
            //TODO 需要支持多admin
            xxlJobSpringExecutor.setAdminAddresses(this.xxlJobProperties.getAdminAddresses()[0]);
            xxlJobSpringExecutor.setName(xxlJobProperties.getName());
            xxlJobSpringExecutor.setTitle(xxlJobProperties.getTitle());
            xxlJobSpringExecutor.setIp(xxlJobProperties.getIp());
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


    }


    @Configuration
   // @ConditionalOnExpression("${spring.boot.xxljob.enabled:false} && ${spring.boot.xxljob.proxyEnabled:false}")
    public static class ProxyXxlJobClientConfiguration {

        private final XxlJobProperties xxlJobProperties;

        public ProxyXxlJobClientConfiguration(XxlJobProperties xxlJobProperties) {
            this.xxlJobProperties = xxlJobProperties;
        }

        @ConditionalOnMissingBean
        @Bean
        public JobHandlerRepository jobHandlerRepository() {
            return new SimpleJobHandlerRepository();
        }

        @ConditionalOnMissingBean
        @Bean
        public JobThreadRepository jobThreadRepository() {
            return new SimpleJobThreadRepository();
        }

        @ConditionalOnMissingBean
        @Bean
        public Executor executor(JobHandlerRepository jobHandlerRepository,JobThreadRepository jobThreadRepository) {
            return new DefaultExecutor(jobHandlerRepository, jobThreadRepository);
        }

        @Order(Ordered.HIGHEST_PRECEDENCE)
        @ConditionalOnMissingBean
        @Bean(destroyMethod = "destroy")
        public XxlJobExecutor xxlJobExecutor(JobHandlerRepository jobHandlerRepository
                , JobThreadRepository jobThreadRepository, Executor executor, ProxyProperties proxyProperties) {
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

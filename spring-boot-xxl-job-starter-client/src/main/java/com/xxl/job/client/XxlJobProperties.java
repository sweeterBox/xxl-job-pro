package com.xxl.job.client;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

/**
 * @author sweeter
 * @date 2022/11/3
 */
@Configuration
@ConfigurationProperties(prefix = XxlJobProperties.PREFIX)
public class XxlJobProperties {

    public static final String PREFIX = "spring.boot.xxl.job.client";

    private boolean enabled = true;

    private boolean proxyEnabled = false;

    private String[] adminAddresses = new String[] {};

    private String accessToken;

    @Value("${spring.boot.xxl.job.client.name:${spring.application.name:unknown}}")
    private String name;
    /**
     * 名称
     */
    @Value("${spring.boot.xxl.job.client.title:${spring.application.name:unknown}}")
    private String title;

    /**
     * 内部代理地址 将netty的tcp端口使用web服务的http端口进行代理
     */
    private String contextPath;

    private String ip;

    @Value("${spring.boot.xxl.job.client.port:${server.port:}}")
    private Integer port;

    private String logPath;

    private Integer logRetentionDays = 30;

    public String[] getAdminAddresses() {
        return adminAddresses;
    }

    public void setAdminAddresses(String[] adminAddresses) {
        this.adminAddresses = adminAddresses;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAddress() {
        if (StringUtils.isNotBlank(ip) && Objects.nonNull(port)) {
            return "http://" + ip + ":" + port;
        }
        return null;
    }



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public Integer getLogRetentionDays() {
        return logRetentionDays;
    }

    public void setLogRetentionDays(Integer logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public boolean isProxyEnabled() {
        return proxyEnabled;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }
}

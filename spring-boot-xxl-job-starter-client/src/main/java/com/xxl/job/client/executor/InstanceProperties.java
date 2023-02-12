package com.xxl.job.client.executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author sweeter
 * @date 2022/12/3
 */
@ConfigurationProperties(prefix = "spring.boot.admin.client.instance")
public class InstanceProperties {

    @Value("${spring.application.name:spring-boot-application}")
    private String name = "spring-boot-application";

    /**
     * Should the registered urls be built with server.address or with hostname.
     */
    private ClientHostType clientHostType = ClientHostType.CANONICAL_HOST_NAME;

    /**
     * Metadata that should be associated with this application
     */
    private Map<String, String> metadata = new LinkedHashMap<>();
}

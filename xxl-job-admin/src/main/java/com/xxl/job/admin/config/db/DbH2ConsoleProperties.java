package com.xxl.job.admin.config.db;

import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author sweeter
 * @date 2021/6/11
 */
@Primary
@Configuration
@ConfigurationProperties(prefix = "spring.h2.console")
public class DbH2ConsoleProperties extends H2ConsoleProperties {

    private Integer port = 8088;

    private boolean enabled = false;

    private String path;

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean getEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

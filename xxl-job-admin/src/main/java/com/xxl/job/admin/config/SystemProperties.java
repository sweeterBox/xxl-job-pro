package com.xxl.job.admin.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author sweeter
 * @date 2021/6/20
 */
@Configuration
//@ConfigurationProperties(prefix = "spring.boot")
public class SystemProperties {

    @Value("${xxl.job.tablePrefix:'xxl_job_pro_'}")
    private String tablePrefix;

    @Value("${xxl.job.i18n:}")
    private String i18n;

    @Value("${xxl.job.accessToken:}")
    private String accessToken;

    @Value("${spring.mail.from:}")
    private String emailFrom;

    @Value("${xxl.job.triggerpool.fast.max:0}")
    private Integer triggerPoolFastMax;

    @Value("${xxl.job.triggerpool.slow.max:0}")
    private Integer triggerPoolSlowMax;

    @Value("${xxl.job.logretentiondays:0}")
    private Integer logretentiondays;

    @Value("${spring.cloud.discovery.enabled:false}")
    private Boolean cloudEnabled;


    public String getI18n() {
        return i18n;
    }

    public void setI18n(String i18n) {
        this.i18n = i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getEmailFrom() {
        return emailFrom;
    }

    public void setEmailFrom(String emailFrom) {
        this.emailFrom = emailFrom;
    }

    public Integer getTriggerPoolFastMax() {
        return triggerPoolFastMax;
    }

    public void setTriggerPoolFastMax(Integer triggerPoolFastMax) {
        this.triggerPoolFastMax = triggerPoolFastMax;
    }

    public Integer getTriggerPoolSlowMax() {
        return triggerPoolSlowMax;
    }

    public void setTriggerPoolSlowMax(Integer triggerPoolSlowMax) {
        this.triggerPoolSlowMax = triggerPoolSlowMax;
    }

    public Integer getLogretentiondays() {
        return logretentiondays;
    }

    public void setLogretentiondays(Integer logretentiondays) {
        this.logretentiondays = logretentiondays;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public SystemProperties setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public Boolean getCloudEnabled() {
        return cloudEnabled;
    }

    public void setCloudEnabled(Boolean cloudEnabled) {
        this.cloudEnabled = cloudEnabled;
    }
}

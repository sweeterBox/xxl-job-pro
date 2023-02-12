package com.xxl.job.admin.core.conf;

import com.xxl.job.admin.core.scheduler.XxlJobScheduler;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */

@Component
public class XxlJobAdminConfig implements CommandLineRunner, DisposableBean , Ordered {

    private static XxlJobAdminConfig adminConfig = null;
    public static XxlJobAdminConfig getAdminConfig() {
        return adminConfig;
    }


    // ---------------------- XxlJobScheduler ----------------------

    private XxlJobScheduler xxlJobScheduler;


    @Override
    public void destroy() throws Exception {
        xxlJobScheduler.destroy();
    }


    // ---------------------- XxlJobScheduler ----------------------

    // conf
    @Value("${xxl.job.i18n:}")
    private String i18n;

    @Value("${xxl.job.accessToken:}")
    private String accessToken;


    @Resource
    private DataSource dataSource;


    public String getI18n() {
        if (!Arrays.asList("zh_CN", "zh_TC", "en").contains(i18n)) {
            return "zh_CN";
        }
        return i18n;
    }

    public String getAccessToken() {
        return accessToken;
    }


    public DataSource getDataSource() {
        return dataSource;
    }


    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    @Override
    public void run(String... args) throws Exception {
        adminConfig = this;
        xxlJobScheduler = new XxlJobScheduler();
        xxlJobScheduler.init();
    }
}

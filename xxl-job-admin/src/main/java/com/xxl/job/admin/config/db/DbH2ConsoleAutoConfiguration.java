package com.xxl.job.admin.config.db;

import com.xxl.job.utils.IpUtil;
import com.xxl.job.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import javax.annotation.Resource;

/**
 * @author sweeter
 * @date 2021/6/11
 */
@Configuration
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
@ConditionalOnProperty(prefix = "spring.h2.console", name = {"enabled"}, havingValue = "true")
@Slf4j
public class DbH2ConsoleAutoConfiguration {

    @Resource
    private DbH2ConsoleProperties h2ConsoleProperties;

    private Server webServer;


    @EventListener(value = ContextRefreshedEvent.class)
    public void start() throws java.sql.SQLException {
        log.info("starting h2 console at port:{} ", h2ConsoleProperties.getPort());
        String webContextPath = SpringContextUtils.getApplicationContext().getEnvironment().getProperty("server.servlet.context-path", "/");
        String port = SpringContextUtils.getEnvironmentProperty("server.port");
        String webConsole = "http://" + IpUtil.getIp() + ":" + port + webContextPath + h2ConsoleProperties.getPath();
        log.info("database h2 web console {}", webConsole);
        this.webServer = org.h2.tools.Server.createWebServer("-webPort", h2ConsoleProperties.getPort().toString(), "-tcpAllowOthers").start();
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        log.info("stopping h2 console at port: {}", h2ConsoleProperties.getPort());
        this.webServer.stop();
    }
}

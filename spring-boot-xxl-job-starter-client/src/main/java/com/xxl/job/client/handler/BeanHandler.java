package com.xxl.job.client.handler;


import com.xxl.job.enums.AutoRegistryType;

import java.util.Objects;

/**
 *  和com.xxl.job.client.handler.AbstractScheduledTask配合，以通过继承来实现任务
 * @author sweeter
 * @date 2023/2/19
 */
public class BeanHandler extends AbstractHandler {

    private String name;

    private String description;

    private boolean deprecated;

    private String author;

    /**
     * CRON 表达式
     */
    private String cron;

    /**
     * 初始化时启动任务
     */
    private boolean autoStart;

    /**
     * 任务自动注册，当cron不为空时有效
     */
    private AutoRegistryType autoRegistry;

    private final AbstractScheduledTask target;

    public BeanHandler(AbstractScheduledTask target) {
        this.target = target;
        this.name = target.name();
        this.description = target.description();
        this.author = target.author();
        this.deprecated = Objects.nonNull(target.getClass().getAnnotation(Deprecated.class));
        this.autoRegistry = target.autoRegistry();
        this.cron = target.cron();
        this.autoStart = target.autoStart();
    }

    public BeanHandler(AbstractScheduledTask target,boolean deprecated) {
        this.target = target;
        this.name = target.name();
        this.description = target.description();
        this.author = target.author();
        this.deprecated = deprecated;
        this.autoRegistry = target.autoRegistry();
        this.cron = target.cron();
        this.autoStart = target.autoStart();
    }

    @Override
    public void execute() throws Exception {
        this.target.execute();

    }

    @Override
    public void init() throws Exception {
        this.target.init();
    }

    @Override
    public void destroy() throws Exception {
        this.target.destroy();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
    }

    public AutoRegistryType getAutoRegistry() {
        return autoRegistry;
    }

    public void setAutoRegistry(AutoRegistryType autoRegistry) {
        this.autoRegistry = autoRegistry;
    }
}

package com.xxl.job.client.handler;

import com.xxl.job.enums.AutoRegistryType;

import java.lang.reflect.Method;

/**
 * 和com.xxl.job.client.annotation.ScheduledTask配合使用，将com.xxl.job.client.annotation.ScheduledTask注解标记在spring bean 的方法上可实现任务调度
 * @author xuxueli 2019-12-11 21:12:18
 */
public class MethodHandler extends AbstractHandler {

    private String name;

    private String description;

    private String author;

    private boolean deprecated = false;

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

    private final Object target;

    private final Method method;

    private Method initMethod;

    private Method destroyMethod;

    public MethodHandler(Object target, Method method, Method initMethod, Method destroyMethod, String name, String description,String author) {
        this.target = target;
        this.method = method;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
        this.name = name;
        this.description = description;
        this.author = author;
    }

    public MethodHandler(Object target, Method method, Method initMethod, Method destroyMethod, String name, String description) {
        this.target = target;
        this.method = method;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
        this.name = name;
        this.description = description;
    }

    public MethodHandler(Object target, Method method, Method initMethod, Method destroyMethod, String name, String description, boolean deprecated) {
        this.target = target;
        this.method = method;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
        this.name = name;
        this.description = description;
        this.deprecated = deprecated;
    }

    public MethodHandler(Object target, Method method, Method initMethod, Method destroyMethod, String name, String description, boolean deprecated,String author,String cron,boolean autoStart,AutoRegistryType autoRegistry) {
        this.target = target;
        this.method = method;
        this.initMethod = initMethod;
        this.destroyMethod = destroyMethod;
        this.name = name;
        this.description = description;
        this.deprecated = deprecated;
        this.author = author;
        this.cron = cron;
        this.autoStart = autoStart;
        this.autoRegistry = autoRegistry;
    }

    @Override
    public void execute() throws Exception {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length > 0) {
            // method-param can not be primitive-types
            method.invoke(target, new Object[paramTypes.length]);
        } else {
            method.invoke(target);
        }
    }

    @Override
    public void init() throws Exception {
        if(initMethod != null) {
            initMethod.invoke(target);
        }
    }

    @Override
    public void destroy() throws Exception {
        if(destroyMethod != null) {
            destroyMethod.invoke(target);
        }
    }

    @Override
    public String toString() {
        return super.toString()+"["+ target.getClass() + "#" + method.getName() +"]";
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

package com.xxl.job.model;

import com.xxl.job.enums.AutoRegistryType;
import lombok.Data;

/**
 * @author sweeter
 * @date 2023/3/12
 */
@Data
public class TaskRegistry {

    private String applicationName;

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
     *
     */
    private AutoRegistryType autoRegistry;
}

package com.xxl.job.client.executor.model;

import lombok.Data;

/**
 * @author sweeter
 * @date 2022/12/4
 */
@Data
public class TaskInfo {

    private String name;

    private String description;

    /**
     * 已过期
     */
    private boolean deprecated = false;

    private String author;

    private String cron;

}

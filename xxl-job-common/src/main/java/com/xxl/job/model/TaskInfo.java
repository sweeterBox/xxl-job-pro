package com.xxl.job.model;

import lombok.Data;

/**
 * @author sweeter
 * @date 2022/12/4
 */
@Data
public class TaskInfo {

    private String name;

    private String title;

    /**
     * 已过期
     */
    private boolean deprecated = false;
}

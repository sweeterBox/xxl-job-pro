package com.xxl.job.enums;

import lombok.Getter;

/**
 * @author sweeter
 * @date 2023/3/12
 */
@Getter
public enum AutoRegistryType {

    /**
     * 不进行自动注册
     */
    NONE,
    /**
     * 创建任务，任务不存在时自动注册，任务存在时不做任何处理
     */
    CREATE,
    /**
     * 更新任务，任务不存在时自动注册，任务存在时更新任务
     */
    UPDATE
}

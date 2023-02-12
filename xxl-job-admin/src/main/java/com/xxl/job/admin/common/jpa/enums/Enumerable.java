package com.xxl.job.admin.common.jpa.enums;

import java.io.Serializable;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public interface Enumerable extends Serializable {
    /**
     * 获取值
     * @return value
     */
    int getValue();

    /**
     * 获取描述名称
     * @return name
     */
    String getName();
}

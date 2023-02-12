package com.xxl.job.admin.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxl.job.admin.common.jpa.enums.BaseEnumConverter;
import com.xxl.job.admin.common.jpa.enums.Enumerable;
import lombok.Getter;

/**
 * @author sweeter
 * @description 任务调度启用状态
 * @date 2022/9/4
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum TriggerStatus implements Enumerable {

    DISABLE(0, "停用"),
    ENABLE(1, "启用"),
    ;

    private int value;

    private String name;

    TriggerStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static class Converter extends BaseEnumConverter<TriggerStatus> {

    }
}

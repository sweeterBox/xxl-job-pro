package com.xxl.job.admin.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxl.job.admin.common.jpa.enums.BaseEnumConverter;
import com.xxl.job.admin.common.jpa.enums.Enumerable;
import lombok.Getter;


/**
 * @author sweeter
 * @date 2022/12/24
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ExecutionStatus implements Enumerable {
    /**
     * 业务执行状态
     */
    FAIL(0, "失败"),

    SUCCESS(1, "成功");

    private int value;

    private String name;

    ExecutionStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static class Converter extends BaseEnumConverter<ExecutionStatus> {

    }
}

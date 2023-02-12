package com.xxl.job.admin.common.jpa.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author sweeter
 * 状态枚举类 映射到数据库
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum CommonStatus implements Enumerable {
    /**
     *
     */
    DISABLE(0, "停用"),
    ENABLE(1, "启用"),
    DELETE(-1, "删除");

    private int value;

    private String name;

    CommonStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }
    /**
     * 映射数据库字段时使用
     * Converter内部类
     */
    public static class Converter extends BaseEnumConverter<CommonStatus> {
    }
}


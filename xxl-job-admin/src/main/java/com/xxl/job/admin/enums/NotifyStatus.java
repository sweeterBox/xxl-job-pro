package com.xxl.job.admin.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xxl.job.admin.common.jpa.enums.BaseEnumConverter;
import com.xxl.job.admin.common.jpa.enums.Enumerable;
import lombok.Getter;

/**
 * @author sweeter
 * @date 2023/2/13
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum NotifyStatus implements Enumerable {
    /**
     * 通知状态
     */
    FAIL(-2, "通知失败"),
    NOT(-1, "无需通知"),
    TODO(0, "待通知"),
    NOTIFIED(1, "已通知"),
    ;

    private int value;

    private String name;

    NotifyStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static class Converter extends BaseEnumConverter<NotifyStatus> {

    }
}

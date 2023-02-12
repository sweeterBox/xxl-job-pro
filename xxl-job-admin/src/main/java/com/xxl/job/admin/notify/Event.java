package com.xxl.job.admin.notify;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author sweeter
 * @description 通知事件 任务执行失败、客户端下线等
 * @date 2022/10/18
 */
@Setter
@Getter
public abstract class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 事件id
     */
    private String id;

    /**
     * 事件发生时的时间戳
     */
    private Instant timestamp;



}

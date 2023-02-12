package com.xxl.job.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TriggerParam implements Serializable {

    private static final long serialVersionUID = 42L;

    private Long taskId;

    private String executorHandler;

    private String executorParams;

    private String executorBlockStrategy;

    private Long executorTimeout;

    private Long logId;

    private long logDateTime;

    private String glueType;

    private String glueSource;

    private Long glueUpdatetime;

    private int broadcastIndex;

    private int broadcastTotal;


}

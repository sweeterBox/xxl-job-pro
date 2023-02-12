package com.xxl.job.admin.model;

import com.xxl.job.admin.enums.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

/**
 * @author sweeter
 * @date 2022/12/24
 */
@Data
public class TaskInfo {

    private Long id;

    private String applicationName;

    private Long applicationId;

    private String description;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @CreatedDate
    private LocalDateTime createTime;


    private String author;

    // 报警邮件
    private String alarmEmail;

    // 调度配置，值含义取决于调度类型
    private String scheduleConf;

    // 执行器路由策略
    private RouteStrategy executorRouteStrategy;

    // 执行器，任务Handler名称
    private String executorHandler;

    // 执行器，任务参数
    private String executorParam;

    // 阻塞处理策略
    private String executorBlockStrategy;

    /**
     * 任务执行超时时间，单位秒
     */
    private Long executorTimeout;

    /**
     * 失败重试次数
     */
    private Integer executorFailRetryCount;

    // GLUE源代码
    private String glueSource;

    // GLUE备注
    private String glueRemark;

    // GLUE更新时间
    private LocalDateTime glueUpdatetime;

    // 子任务ID，多个逗号分隔
    private String childJobId;

    private ScheduleType scheduleType;

    private MisfireStrategy misfireStrategy;

    private GlueType glueType;

    private TriggerStatus triggerStatus;

    /**
     * 	上次调度时间
     */
    private Long lastTriggerTime;

    /**
     * 下次调度时间
     */
    private Long nextTriggerTime;

    private ExecutionStatus lastExecutionStatus;

    private ScheduleStatus lastScheduleStatus;
}

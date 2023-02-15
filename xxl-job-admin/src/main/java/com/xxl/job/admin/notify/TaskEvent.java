package com.xxl.job.admin.notify;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * @author sweeter
 * @date 2023/1/20
 */
@Setter
@Getter
public class TaskEvent extends Event {

    private Task task;

    private Log log;



    @Data
    public static class Task{

        private String applicationName;

        private String description;

        private String author;

        private String executorHandler;
    }

    @Data
    public static class Log {

        private String instanceUrl;

        private String executorHandler;

        private String executorParam;

        private String executorShardingParam;

        private Integer executorFailRetryCount;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime triggerTime;

        /**
         * 任务调度状态
         */
        private Integer triggerStatus;

        /**
         * 任务调度内容
         */
        private String triggerContent;

        /**
         * 执行开始时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime handleStartTime;

        /**
         * 执行结束时间
         */
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS",timezone = "GMT+8")
        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime handleEndTime;

        /**
         * 执行状态
         */
        private Integer handleStatus;

        /**
         * 执行日志内容
         */
        private String handleContent;
    }

}

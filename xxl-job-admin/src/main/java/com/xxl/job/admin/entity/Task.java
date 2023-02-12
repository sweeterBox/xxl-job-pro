package com.xxl.job.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xxl.job.admin.common.jpa.entity.IdEntity;
import com.xxl.job.admin.enums.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * xxl-job info
 *
 * @author xuxueli  2016-1-12 18:25:49
 */
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@Table(name = "task")
@Entity
public class Task extends IdEntity {

	private String applicationName;

	@Column(columnDefinition ="varchar(20) COMMENT '任务描述'")
	private String description;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@LastModifiedDate
	private LocalDateTime updateTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@CreatedDate
	private LocalDateTime createTime;

	@Column(columnDefinition ="varchar(20) COMMENT '负责人or作者'")
	private String author;

	private String alarmEmail;

	@Enumerated(EnumType.STRING)
	private ScheduleType scheduleType;

	@Column(columnDefinition ="varchar(128) COMMENT '调度配置'")
	private String scheduleConf;


	@Column(columnDefinition ="varchar(50) COMMENT '调度过期策略'")
	@Enumerated(EnumType.STRING)
	private MisfireStrategy misfireStrategy;

	@Enumerated(EnumType.STRING)
	private RouteStrategy executorRouteStrategy;


	@Column(columnDefinition ="varchar(50) COMMENT '任务Handler名称'")
	private String executorHandler;

	@Column(columnDefinition ="varchar(255) COMMENT '任务参数'")
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

	@Enumerated(EnumType.STRING)
	private GlueType glueType;

	// GLUE源代码
	private String glueSource;

	// GLUE备注
	private String glueRemark;

	// GLUE更新时间
	private LocalDateTime glueUpdatetime;

	/**
	 * 	子任务ID，多个逗号分隔
 	 */
	private String childJobId;

	@Convert(converter = TriggerStatus.Converter.class)
	private TriggerStatus triggerStatus;

	/**
	 * 	上次调度时间
	 */
	private Long lastTriggerTime;

	/**
	 * 下次调度时间
	 */
	private Long nextTriggerTime;

	@Convert(converter = ExecutionStatus.Converter.class)
	private ExecutionStatus lastExecutionStatus;

	@Convert(converter = ScheduleStatus.Converter.class)
	private ScheduleStatus lastScheduleStatus;


}

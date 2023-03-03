package com.xxl.job.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xxl.job.admin.common.jpa.entity.IdEntity;
import com.xxl.job.admin.enums.NotifyStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 调度日志
 * xxl-job log, used to track trigger process
 * @author xuxueli  2015-12-19 23:19:09
 */
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@Table(name = "log")
@Entity
public class Log extends IdEntity {

	private String applicationName;

	private Long taskId;

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
	//@Column(columnDefinition = "TEXT")
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
	//@Column(columnDefinition = "TEXT")
	private String handleContent;

	/**
	 * 通知状态
	 */
	@Convert(converter = NotifyStatus.Converter.class)
	private NotifyStatus notifyStatus;



}

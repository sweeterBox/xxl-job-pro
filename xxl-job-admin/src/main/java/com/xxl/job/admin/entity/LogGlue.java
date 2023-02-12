package com.xxl.job.admin.entity;

import com.xxl.job.admin.common.jpa.entity.IdEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.Date;

/**
 * xxl-job log for glue, used to track job code process
 * @author xuxueli 2016-5-19 17:57:46
 */
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@Table(name = "logglue")
@Entity
public class LogGlue extends IdEntity {


	private Long taskId;				// 任务主键ID

	private String glueType;		// GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum

	private String glueSource;

	private String glueRemark;

	private Date addTime;

	private Date updateTime;



}

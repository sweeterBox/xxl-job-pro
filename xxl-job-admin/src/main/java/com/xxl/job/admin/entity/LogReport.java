package com.xxl.job.admin.entity;

import com.xxl.job.admin.common.jpa.entity.IdEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.Date;

@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@Table(name = "log_report")
@Entity
public class LogReport extends IdEntity {

    private Date triggerDay;

    private Integer runningCount;

    private Integer sucCount;

    private Integer failCount;


}

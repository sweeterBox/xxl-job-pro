package com.xxl.job.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xxl.job.admin.common.jpa.entity.IdEntity;
import com.xxl.job.admin.enums.InstanceStatus;
import com.xxl.job.enums.ClientHostType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@Table(name = "instance")
@Entity
public class Instance extends IdEntity {

    @Column(columnDefinition ="varchar(255) COMMENT ''",nullable = false)
    private String url;

    @Column(columnDefinition ="varchar(50) COMMENT ''",nullable = false)
    private String name;

    @Column(columnDefinition ="varchar(255) COMMENT ''",nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition ="varchar(20) COMMENT ''",nullable = false)
    private ClientHostType clientHostType;

    @Column(columnDefinition ="bit(1) COMMENT '是否健康'",nullable = false)
    private Boolean healthy;

    @Column(columnDefinition ="bit(1) COMMENT '是否是临时实例'",nullable = false)
    private Boolean ephemeral;

    private Double weight;

    private String host;

    private Integer port;

    private Boolean secure;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition ="varchar(4) COMMENT '实例状态'")
    private InstanceStatus status;

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
}

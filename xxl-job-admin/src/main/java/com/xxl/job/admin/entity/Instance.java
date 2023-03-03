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

    @Column(nullable = false)
    private String url;

    @Column(length = 50,nullable = false)
    private String name;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(length = 20,nullable = false)
    private ClientHostType clientHostType;

    /**
     * 是否健康
     */
    @Column(nullable = false)
    private Boolean healthy;

    /**
     * 是否是临时实例
     */
    @Column(nullable = false)
    private Boolean ephemeral;

    @Column(scale = 1)
    private Double weight;

    private String host;

    private Integer port;

    private Boolean secure;

    /**
     * 实例状态
     */
    @Enumerated(EnumType.STRING)
    @Column(length = 4)
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

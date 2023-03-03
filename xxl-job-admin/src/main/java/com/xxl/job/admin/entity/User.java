package com.xxl.job.admin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.xxl.job.admin.common.jpa.entity.IdEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author sweeter
 * @description 用户信息 user info
 * @date 2022/9/3
 */
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
@Setter
@Table(name = "user"
		,indexes= {
		@Index(name = "xxl_job_user_username_index", columnList = "username")
})
@Entity
public class User extends IdEntity {

	@Column(length = 32,nullable = false,updatable = false,unique = true)
	private String username;

	@Column(length = 32,nullable = false)
	private String password;

	/**
	 * 角色：0-普通用户、1-管理员
	 */
	@Column(length = 1)
	private Integer role;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@LastModifiedDate
	private LocalDateTime updateTime;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@CreatedDate
	private LocalDateTime createTime;

}

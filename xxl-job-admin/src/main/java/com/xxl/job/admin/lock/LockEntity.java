package com.xxl.job.admin.lock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sweeter
 * @date 2023/1/15
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "lock")
@Entity
public class LockEntity {

    @Id
    private String lockKey;
    /**
     * 默认锁过期时间 单位/毫秒
     */
    private Long expireTime;
    /**
     * 默认取锁超时时间 单位/毫秒
     */
    private Long timeOut;

    /**
     * 锁创建时间戳
     */
    private Long createTimestamp;
}

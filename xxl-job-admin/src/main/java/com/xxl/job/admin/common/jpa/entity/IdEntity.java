package com.xxl.job.admin.common.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author sweeter
 * @date 2021/6/20
 * id生成
 */
@Getter
@Setter
@MappedSuperclass
public abstract class IdEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
}

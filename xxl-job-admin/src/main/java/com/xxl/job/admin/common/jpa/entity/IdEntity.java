package com.xxl.job.admin.common.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

/**
 * @author sweeter
 * @date 2021/6/20
 * id生成
 */
@Getter
@Setter
@MappedSuperclass
public abstract class IdEntity{

    /**
     * Oracle 不支持IDENTITY
     * Mysql 不支持SEQUENCE
     * @GeneratedValue(strategy = GenerationType.AUTO)时Postgresql全局自增不合理
     *
     * @GenericGenerator(name = "system-uuid", strategy = "uuid") 或许是最佳方案
     * @GeneratedValue(generator = "system-uuid")
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO,generator ="table_id_auto_increment")
    @TableGenerator(
            name = "table_id_auto_increment",
            table="identity_auto_increment",
           // pkColumnName="table_name",
           // pkColumnValue="auto_increment",
           // valueColumnName="next_value",
            initialValue = 0,
            allocationSize=1
    )
    protected Long id;
}

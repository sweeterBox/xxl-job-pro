package com.xxl.job.admin.config.db;

import org.hibernate.dialect.MySQLDialect;

/**
 * @author sweeter
 * @date 2023/1/15
 */
public class MySQLInnoDBDialect extends MySQLDialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}

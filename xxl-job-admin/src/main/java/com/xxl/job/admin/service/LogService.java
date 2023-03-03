package com.xxl.job.admin.service;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.query.LogQuery;

/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface LogService {


    ResultPage<Log> findPageList(LogQuery query);

    Log findOne(Long id);

    void save(Log entity);
}

package com.xxl.job.admin.service;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.Application;
import com.xxl.job.admin.model.ApplicationInfo;
import com.xxl.job.admin.query.ApplicationQuery;

import java.util.List;

/**
 * @author sweeter
 * @description
 * @date 2022/9/3
 */
public interface ApplicationService {

    ResultPage<ApplicationInfo> findPageList(ApplicationQuery query);

    void delete(Long id);

    ApplicationInfo findOne(Long id);

    ApplicationInfo findOne(String name);

    void save(Application application);

    List<Application> findAll();

}

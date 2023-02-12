package com.xxl.job.admin.instance;

import com.xxl.job.admin.entity.Application;
import com.xxl.job.admin.entity.Instance;

import java.util.List;

/**
 * @author sweeter
 * @date 2023/1/14
 */
public interface DiscoveryInstance {

    /**
     * 查询所有应用
     * @return all application
     */
    List<Application> findAllApplications();

    /**
     * 查询所有实例
     * @param applicationName 应用名
     * @return all instance
     */
    List<Instance> findAllInstances(String applicationName);
}

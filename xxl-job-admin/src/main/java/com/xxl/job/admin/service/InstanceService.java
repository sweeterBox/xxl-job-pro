package com.xxl.job.admin.service;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.query.InstanceQuery;
import java.util.List;

/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface InstanceService {

    /**
     * 查询所有的应用实例
     * @return List<Instance>
     */
    List<Instance> findAll(String applicationName);

    ResultPage<Instance> findPageList(InstanceQuery query);
}

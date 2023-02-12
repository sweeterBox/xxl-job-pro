package com.xxl.job.admin.cloud.listener;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

/**
 * @author sweeter
 * @date 2023/1/14
 */
@Slf4j
public class XxlJobAdminUpAndDownListener implements InstanceUpAndDownListener {


    /**
     * 实例上线通知
     *
     * @param instance  当前实例信息
     * @param instances 所有在线的可用实例
     */
    @Override
    public void up(com.alibaba.nacos.api.naming.pojo.Instance instance, List<com.alibaba.nacos.api.naming.pojo.Instance> instances) {


    }

    /**
     * 实例下线通知
     *
     * @param instance  当前实例信息
     * @param instances 所有在线的可用实例
     */
    @Override
    public void down(com.alibaba.nacos.api.naming.pojo.Instance instance, List<com.alibaba.nacos.api.naming.pojo.Instance> instances) {

    }
}

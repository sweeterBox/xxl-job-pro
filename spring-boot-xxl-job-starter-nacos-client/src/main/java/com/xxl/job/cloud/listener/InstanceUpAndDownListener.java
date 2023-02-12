package com.xxl.job.cloud.listener;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author sweeter
 * @description
 * @date 2022/12/27 CompositeInstanceUpAndDownListenerAutoConfiguration
 */
public interface InstanceUpAndDownListener {

    /**
     * 实例上线通知
     * @param instance 当前实例信息
     * @param instances 所有在线的可用实例
     */
    void up(Instance instance, List<Instance> instances);

    /**
     * 实例下线通知
     * @param instance 当前实例信息
     * @param instances 所有在线的可用实例
     */
    void down(Instance instance, List<Instance> instances);

}

package com.xxl.job.admin.cloud.listener;

import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import java.util.Collections;
import java.util.List;

/**
 * @author sweeter
 * @description
 * @date 2022/12/27
 */
@Slf4j
public class CompositeInstanceUpAndDownListener implements InstanceUpAndDownListener{

    private final List<InstanceUpAndDownListener> allUpAndDownListener;

    public CompositeInstanceUpAndDownListener(List<InstanceUpAndDownListener> allUpAndDownListener) {
        this.allUpAndDownListener = allUpAndDownListener;
    }

    /**
     * 实例上线通知
     *
     * @param instance  当前实例信息
     * @param instances 所有在线的可用实例
     */
    @Override
    public void up(Instance instance, List<Instance> instances) {
        this.getUpAndDownListeners().forEach(listener -> {
            try {
                listener.up(instance, instances);
            } catch (Throwable e) {
                log.error("CompositeInstanceUpAndDownListener up  exception:{}",e.getMessage());
            }
        });
    }

    /**
     * 实例下线通知
     *
     * @param instance  当前实例信息
     * @param instances 所有在线的可用实例
     */
    @Override
    public void down(Instance instance, List<Instance> instances) {
        this.getUpAndDownListeners().forEach(listener -> {
            try {
                listener.down(instance, instances);
            } catch (Throwable e) {
                log.error("CompositeInstanceUpAndDownListener down exception:{}",e.getMessage());
            }
        });
    }

    private List<InstanceUpAndDownListener> getUpAndDownListeners() {
        List<InstanceUpAndDownListener> instanceUpAndDownListeners = this.allUpAndDownListener;
        return !CollectionUtils.isEmpty(instanceUpAndDownListeners) ? instanceUpAndDownListeners : Collections.emptyList();
    }
}

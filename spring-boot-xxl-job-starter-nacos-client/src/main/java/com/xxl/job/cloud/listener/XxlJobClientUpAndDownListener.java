package com.xxl.job.cloud.listener;


import com.xxl.job.client.XxlJobProperties;
import com.xxl.job.client.task.InstanceRegistryThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

//import lombok.extern.slf4j.Slf4j;

/**
 * @author sweeter
 * @date 2023/1/14
 */
//@Slf4j
public class XxlJobClientUpAndDownListener implements InstanceUpAndDownListener {

    @Autowired
    private XxlJobProperties xxlJobProperties;

    private final String xxlJobClientKey = "xxl.job.client";

    private final String titleKey = "xxl.job.client.title";

    private final String contextPathKey = "xxl.job.client.contextPath";


    /**
     * 实例上线通知
     *
     * @param instance  当前实例信息
     * @param instances 所有在线的可用实例
     */
    @Override
    public void up(com.alibaba.nacos.api.naming.pojo.Instance instance, List<com.alibaba.nacos.api.naming.pojo.Instance> instances) {
        String name = xxlJobProperties.getName();
        Map<String, String> metadata = instance.getMetadata();
        boolean isClient = Optional.ofNullable(metadata).filter(m -> m.containsKey(xxlJobClientKey))
                .map(m -> "true".equals(m.get(xxlJobClientKey))).orElse(false);
        if (isClient) {
            String contextPath = this.getContextPath(metadata);
            String title = this.getTitle(metadata);
            String url = UriComponentsBuilder.newInstance()
                    .scheme("http")
                    .host(instance.getIp())
                    .port(instance.getPort())
                    .path(contextPath)
                    .build()
                    .toUriString();
            InstanceRegistryThread.getInstance().start(name, url, title);
        }
    }

    private String getContextPath(Map<String, String> metadata) {
        String contextPath = Optional.ofNullable(metadata).filter(m -> m.containsKey(contextPathKey))
                .map(m -> m.get(contextPathKey)).orElse(xxlJobProperties.getContextPath());
        return contextPath;
    }

    private String getTitle(Map<String, String> metadata) {
        String title = Optional.ofNullable(metadata).filter(m -> m.containsKey(titleKey))
                .map(m -> m.get(titleKey)).orElse(xxlJobProperties.getTitle());
        return title;
    }


    /**
     * 实例下线通知
     *
     * @param instance  当前实例信息
     * @param instances 所有在线的可用实例
     */
    @Override
    public void down(com.alibaba.nacos.api.naming.pojo.Instance instance, List<com.alibaba.nacos.api.naming.pojo.Instance> instances) {
        InstanceRegistryThread.getInstance().toStop();
    }
}

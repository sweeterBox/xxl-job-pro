package com.xxl.job.admin.cloud.listener;

import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.enums.InstanceStatus;
import com.xxl.job.admin.repository.InstanceRepository;
import com.xxl.job.enums.ClientHostType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author sweeter
 * @date 2023/1/14
 */
@Slf4j
public class XxlJobAdminUpAndDownListener implements InstanceUpAndDownListener {

    @Resource
    private InstanceRepository instanceRepository;

    /**
     * 实例上线通知
     *
     * @param instance  当前实例信息
     * @param instances 所有在线的可用实例
     */
    @Override
    public void up(com.alibaba.nacos.api.naming.pojo.Instance instance, List<com.alibaba.nacos.api.naming.pojo.Instance> instances) {


    }

    private Instance loadInstance(com.alibaba.nacos.api.naming.pojo.Instance cloudInstance) {
        String xxlJobClientKey = "xxl.job.client";
        String xxlJobAdmin = "xxl.job.admin";
        String weightKey = "nacos.weight";
        String ephemeralKey = "nacos.ephemeral";
        String healthyKey = "nacos.healthy";
        String contextPathKey = "xxl.job.client.contextPath";
        String titleKey = "xxl.job.client.title";

        Instance instance = new Instance();
        instance.setHost(cloudInstance.getIp());
        instance.setPort(cloudInstance.getPort());
        instance.setClientHostType(ClientHostType.CANONICAL_HOST_NAME);
        instance.setEphemeral(cloudInstance.isEphemeral());
        instance.setHealthy(cloudInstance.isHealthy());
        instance.setStatus(InstanceStatus.UP);
        instance.setTitle(cloudInstance.getServiceName());
        instance.setName(cloudInstance.getServiceName());
        Map<String, String> metadata = cloudInstance.getMetadata();
        String contextPath = Optional.ofNullable(metadata).filter(m -> m.containsKey(contextPathKey))
                .map(m -> m.get(contextPathKey)).orElse("/xxl-job");
        String title = Optional.ofNullable(metadata).filter(m -> m.containsKey(titleKey))
                .map(m -> m.get(titleKey)).orElse(cloudInstance.getServiceName());

/*        String url = UriComponentsBuilder.fromUri(cloudInstance.get)
                .path(contextPath)
                .build()
                .toUriString();*/
        return instance;
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

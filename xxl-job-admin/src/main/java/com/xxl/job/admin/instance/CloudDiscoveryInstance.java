package com.xxl.job.admin.instance;

import com.xxl.job.utils.SpringContextUtils;
import com.xxl.job.admin.entity.Application;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.enums.InstanceStatus;
import com.xxl.job.enums.ClientHostType;
import com.xxl.job.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sweeter
 * @date 2023/1/14
 */
@Slf4j
public class CloudDiscoveryInstance implements DiscoveryInstance {

    @Resource
    private DiscoveryClient discoveryClient;

    @Override
    public List<Application> findAllApplications() {
        String xxlJobAdminServiceId = SpringContextUtils.getEnvironmentProperty("spring.application.name");
        List<String> services = discoveryClient.getServices();
        services = services.stream().filter(v -> !StringUtils.equals(v, xxlJobAdminServiceId)).collect(Collectors.toList());
        for (String service : services) {
            Application application = new Application();
            application.setName(service);
            application.setTitle(service);
        }
        return Collections.emptyList();
    }


    @Override
    public List<Instance> findAllInstances(String applicationName) {
        List<Instance> instances = new ArrayList<>();
        String xxlJobAdminServiceId = SpringContextUtils.getEnvironmentProperty("spring.application.name");
        String xxlJobClientKey = "xxl.job.client";
        String xxlJobAdmin = "xxl.job.admin";
        String weightKey = "nacos.weight";
        String ephemeralKey = "nacos.ephemeral";
        String healthyKey = "nacos.healthy";
        String contextPathKey = "xxl.job.client.contextPath";
        String titleKey = "xxl.job.client.title";
        List<String> services = discoveryClient.getServices();
        services = services.stream()
                .filter(v -> !StringUtils.equals(v, xxlJobAdminServiceId))
                .filter(v -> (StringUtils.isNotBlank(applicationName) && StringUtils.equals(applicationName, v)) || StringUtils.isBlank(applicationName))
                .collect(Collectors.toList());
        for (String service : services) {
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(service);
            log.info("service {} instance info:{}", service, JsonUtils.obj2Json(serviceInstances));
            List<Instance> instanceList = serviceInstances.stream()
                    .filter(v -> v.getMetadata().containsKey(xxlJobClientKey)
                            && StringUtils.equals(v.getMetadata().get(xxlJobClientKey), "true"))
                    .map(v -> {
                        Instance instance = new Instance();
                        instance.setPort(v.getPort());
                        instance.setHost(v.getHost());
                        instance.setStatus(InstanceStatus.UP);
                        instance.setClientHostType(ClientHostType.CANONICAL_HOST_NAME);
                        instance.setName(v.getServiceId());
                        instance.setName(v.getServiceId());
                        Map<String, String> metadata = v.getMetadata();
                        if (Objects.nonNull(metadata)) {

                            boolean healthy = Optional.ofNullable(metadata).filter(m -> m.containsKey(healthyKey))
                                    .map(m -> StringUtils.equals(m.get(healthyKey), "true")).orElse(false);

                            boolean ephemeral = Optional.ofNullable(metadata).filter(m -> m.containsKey(ephemeralKey))
                                    .map(m -> StringUtils.equals(m.get(ephemeralKey), "true")).orElse(false);

                            double weight = Optional.ofNullable(metadata).filter(m -> m.containsKey(weightKey))
                                    .map(m -> Double.parseDouble(m.get(weightKey))).orElse(1.0);

                            String contextPath = Optional.ofNullable(metadata).filter(m -> m.containsKey(contextPathKey))
                                    .map(m -> m.get(contextPathKey)).orElse("/xxl-job");
                            String title = Optional.ofNullable(metadata).filter(m -> m.containsKey(titleKey))
                                    .map(m -> m.get(titleKey)).orElse(v.getServiceId());

                            String url = UriComponentsBuilder.fromUri(v.getUri())
                                    .path(contextPath)
                                    .build()
                                    .toUriString();
                            instance.setUrl(url);
                            instance.setHealthy(healthy);
                            instance.setEphemeral(ephemeral);
                            instance.setWeight(weight);
                            instance.setTitle(title);
                        }
                        return instance;
                    }).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(instanceList)) {
                instances.addAll(instanceList);
            }
        }
        return instances;
    }


}

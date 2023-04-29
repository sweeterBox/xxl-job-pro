package com.xxl.job.cloud;

import com.xxl.job.client.executor.client.AdminApiClient;
import com.xxl.job.client.executor.model.HandleCallbackParam;
import com.xxl.job.client.executor.model.InstanceRegistry;
import com.xxl.job.model.R;
import com.xxl.job.model.TaskRegistry;
import com.xxl.job.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author sweeter
 * @date 2023/1/15
 */
public class AdminApiLoadBalancedClient implements AdminApiClient {

   private final static String XXL_JOB_ADMIN_KEY = "xxl.job.admin";

   private final static String XXL_JOB_ADMIN_CONTEXT_PATH_KEY = "xxl.job.admin.contextPath";

    private String adminServiceUrl;


    @Resource
    private DiscoveryClient discoveryClient;

    @Resource(name = "xxlLoadBalancedRestTemplate")
    private RestTemplate xxlLoadBalancedRestTemplate;
    /**
     * callback
     *
     * @param params
     * @return
     */
    @Override
    public R<String> callback(List<HandleCallbackParam> params) {
        String adminUrl = this.getAdminService() + "/api/callback";
        ResponseEntity<String> responseEntity = xxlLoadBalancedRestTemplate.postForEntity(adminUrl, params, String.class);
        return JsonUtils.json2Obj(responseEntity.getBody(), R.class, String.class);
    }

    /**
     * 将客户端应用实例注册到admin server
     *
     * @param instance 实例
     * @return
     */
    @Override
    public R<String> register(InstanceRegistry instance) {
        String adminUrl = this.getAdminService() + "/api/registry";
        ResponseEntity<String> responseEntity = xxlLoadBalancedRestTemplate.postForEntity(adminUrl, instance, String.class);
        return JsonUtils.json2Obj(responseEntity.getBody(), R.class, String.class);
    }

    /**
     * 从admin server中移除实例，即：实例下线
     *
     * @param instance
     * @return
     */
    @Override
    public R<String> deregister(InstanceRegistry instance) {
        String adminUrl = this.getAdminService() + "/api/deregister";
        ResponseEntity<String> responseEntity = xxlLoadBalancedRestTemplate.postForEntity(adminUrl, instance, String.class);
        return JsonUtils.json2Obj(responseEntity.getBody(), R.class, String.class);
    }

    /**
     * 将任务注册到admin server
     *
     * @param taskRegistry
     * @return
     */
    @Override
    public R<String> saveTask(TaskRegistry taskRegistry) {
        String adminUrl = this.getAdminService() + "/api/saveTask";
        ResponseEntity<String> responseEntity = xxlLoadBalancedRestTemplate.postForEntity(adminUrl, taskRegistry, String.class);
        return JsonUtils.json2Obj(responseEntity.getBody(), R.class, String.class);
    }

    private String getAdminService() {
        if (StringUtils.isBlank(this.adminServiceUrl)) {
            Set<String> adminServiceUrls = new HashSet<>();
            List<String> services = discoveryClient.getServices();
            for (String service : services) {
                List<ServiceInstance> serviceInstances = discoveryClient.getInstances(service);
                if (serviceInstances.stream().anyMatch(v -> v.getMetadata().containsKey(XXL_JOB_ADMIN_KEY)
                        && StringUtils.equals(v.getMetadata().get(XXL_JOB_ADMIN_KEY), "true"))) {
                    String contextPath = serviceInstances.stream()
                            .filter(v -> v.getMetadata().containsKey(XXL_JOB_ADMIN_CONTEXT_PATH_KEY))
                            .map(v -> v.getMetadata().get(XXL_JOB_ADMIN_CONTEXT_PATH_KEY))
                            .findAny().orElse("");
                    if (!StringUtils.startsWith(contextPath, "/")) {
                        contextPath = "/" + contextPath;
                    }
                    adminServiceUrls.add("http://" + service + contextPath);
                }
            }
            if (adminServiceUrls.size() > 1) {
                throw new IllegalStateException("Multiple admin services are available");
            }
            this.adminServiceUrl = adminServiceUrls.stream().findAny().orElse(null);
        }

        if (StringUtils.isNotBlank(this.adminServiceUrl)) {
            return this.adminServiceUrl;
        }
        throw new IllegalStateException("No instances available for xxl-job-admin");
    }

}

package com.xxl.job.cloud;

import com.xxl.job.client.executor.client.AdminApiClient;
import com.xxl.job.client.executor.model.HandleCallbackParam;
import com.xxl.job.client.executor.model.InstanceRegistry;
import com.xxl.job.model.R;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

/**
 * @author sweeter
 * @date 2023/1/15
 */
public interface AdminApiFeignClient extends AdminApiClient {

    /**
     * 将客户端应用实例注册到admin server
     *
     * @param instance 实例
     * @return
     */
    @PostMapping("api/registry")
    @Override
    R<String> register(InstanceRegistry instance);

    /**
     * 从admin server中移除客户端应用实例，即：实例下线
     *
     * @param instance
     * @return
     */
    @PostMapping("api/deregister")
    @Override
    R<String> deregister(InstanceRegistry instance);

    /**
     * 任务执行结果回调
     *
     * @param params
     * @return
     */
    @PostMapping("api/callback")
    @Override
    R<String> callback(List<HandleCallbackParam> params);
}

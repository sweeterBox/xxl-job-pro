package com.xxl.job.client.executor.client;

import com.xxl.job.client.executor.model.HandleCallbackParam;
import com.xxl.job.client.executor.model.InstanceRegistry;
import com.xxl.job.model.R;
import com.xxl.job.model.TaskRegistry;
import java.util.List;

/**
 * @author xuxueli 2017-07-27 21:52:49
 */
public interface AdminApiClient {

    /**
     * callback
     *
     * @param params
     * @return
     */
     R<String> callback(List<HandleCallbackParam> params);


    /**
     * 将客户端应用实例注册到admin server
     *
     * @param instance 实例
     * @return
     */
     R<String> register(InstanceRegistry instance);

    /**
     * 从admin server中移除实例，即：实例下线
     *
     * @param instance
     * @return
     */
     R<String> deregister(InstanceRegistry instance);

    /**
     * 将任务注册到admin server
     * @param taskRegistry
     * @return
     */
    R<String> saveTask(TaskRegistry taskRegistry);


}

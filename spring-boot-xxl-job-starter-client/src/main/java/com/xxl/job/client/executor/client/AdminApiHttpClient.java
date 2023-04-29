package com.xxl.job.client.executor.client;

import com.xxl.job.client.executor.model.HandleCallbackParam;
import com.xxl.job.client.executor.model.InstanceRegistry;
import com.xxl.job.model.R;
import com.xxl.job.model.TaskRegistry;
import com.xxl.job.utils.HttpClient;
import java.util.List;

/**
 * admin api
 *
 * @author xuxueli 2017-07-28 22:14:52
 */
public class AdminApiHttpClient implements AdminApiClient {

    public AdminApiHttpClient() {

    }
    public AdminApiHttpClient(String adminServerUrl, String accessToken) {
        this.adminServerUrl = adminServerUrl;
        this.accessToken = accessToken;
        // valid
        if (!this.adminServerUrl.endsWith("/")) {
            this.adminServerUrl = this.adminServerUrl + "/";
        }
    }

    private String adminServerUrl ;
    private String accessToken;
    private int timeout = 3;


    @Override
    public R<String> callback(List<HandleCallbackParam> callbackParamList) {
        //回调任务执行结果
        return HttpClient.postBody(adminServerUrl+"api/callback", accessToken, timeout, callbackParamList, String.class);
    }

    @Override
    public R<String> register(InstanceRegistry registryParam) {

        return HttpClient.postBody(adminServerUrl + "api/registry", accessToken, timeout, registryParam, String.class);
    }

    @Override
    public R<String> deregister(InstanceRegistry registryParam) {
        return HttpClient.postBody(adminServerUrl + "api/deregister", accessToken, timeout, registryParam, String.class);
    }

    /**
     * 将任务注册到admin server
     *
     * @param taskRegistry
     * @return
     */
    @Override
    public R<String> saveTask(TaskRegistry taskRegistry) {
        return HttpClient.postBody(adminServerUrl + "api/saveTask", accessToken, timeout, taskRegistry, String.class);
    }

}

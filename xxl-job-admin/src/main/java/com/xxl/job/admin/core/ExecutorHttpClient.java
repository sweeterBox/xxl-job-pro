package com.xxl.job.admin.core;

import com.xxl.job.model.*;
import com.xxl.job.utils.HttpClient;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * admin api
 *
 * @author xuxueli 2017-07-28 22:14:52
 */
public class ExecutorHttpClient implements Executor {

    public ExecutorHttpClient() {
    }
    public ExecutorHttpClient(String clientUrl, String accessToken) {
        this.clientUrl = clientUrl;
        this.accessToken = accessToken;
        if (StringUtils.isNotBlank(this.clientUrl) && !this.clientUrl.endsWith("/")) {
            this.clientUrl = this.clientUrl + "/";
        }
    }

    private String clientUrl ;

    private String accessToken;

    private int timeout = 3;


    @Override
    public R<String> beat() {
        return HttpClient.postBody(clientUrl + "beat", accessToken, timeout, "", String.class);
    }

    @Override
    public R<String> idleBeat(IdleBeatParam idleBeatParam){
        return HttpClient.postBody(clientUrl + "idleBeat", accessToken, timeout, idleBeatParam, String.class);
    }

    @Override
    public R<String> run(TriggerParam triggerParam) {
        return HttpClient.postBody(clientUrl + "run", accessToken, timeout, triggerParam, String.class);
    }

    @Override
    public R<String> kill(KillParam killParam) {
        return HttpClient.postBody(clientUrl + "kill", accessToken, timeout, killParam, String.class);
    }

    @Override
    public R<LogResult> log(LogParam logParam) {
        return HttpClient.postBody(clientUrl + "log", accessToken, timeout, logParam, LogResult.class);
    }

    @Override
    public R<List<TaskInfo>> tasks() {
        return HttpClient.postBody(clientUrl + "tasks", accessToken, timeout, "", List.class);
    }

}

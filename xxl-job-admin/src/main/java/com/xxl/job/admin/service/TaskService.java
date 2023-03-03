package com.xxl.job.admin.service;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.model.ReqExeOnce;
import com.xxl.job.admin.model.TaskInfo;
import com.xxl.job.admin.query.TaskQuery;
import com.xxl.job.model.R;

import java.util.List;

/**
 * @author sweeter
 * @date 2022/9/3
 */
public interface TaskService {

    Task getById(Long id);

    ResultPage<TaskInfo> findPageList(TaskQuery query);

    /**
     *  保存任务
     * @param task
     * @return
     */
    R<String> save(Task task);

    /**
     * 删除任务
     * @param id
     * @return
     */
    R<String> delete(Long id);

    /**
     * 停止任务
     * @param id
     * @return
     */
    R<String> stop(Long id);

    /**
     * 开始任务
     * @param id
     * @return
     */
    R<String> start(Long id);

    /**
     * 执行一次
     * @param param
     */
    void executeOnce(ReqExeOnce param);

    /**
     * 查询客户端任务
     * @param applicationName
     * @return
     */
    List<com.xxl.job.model.TaskInfo> tasks(String applicationName);

    /**
     * 查询可以调度的任务
     * @param maxNextTime
     * @param limitSize
     * @return
     */
    List<Task> findScheduleTasks(Long maxNextTime, Long limitSize);
}

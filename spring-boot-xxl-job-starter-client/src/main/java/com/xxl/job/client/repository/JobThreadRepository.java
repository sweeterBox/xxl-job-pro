package com.xxl.job.client.repository;

import com.xxl.job.client.handler.AbstractJobHandler;
import com.xxl.job.client.task.ScheduledTaskThread;

/**
 * @author sweeter
 * @date 2022/12/10
 */
public interface JobThreadRepository {

    ScheduledTaskThread save(int jobId, ScheduledTaskThread jobThread);

    ScheduledTaskThread save(int jobId, AbstractJobHandler handler, String removeOldReason);

    ScheduledTaskThread remove(int jobId, String removeOldReason);

    ScheduledTaskThread findOne(int jobId);

    void clear();

    void destroy();

}

package com.xxl.job.client.repository;

import com.xxl.job.client.handler.AbstractHandler;
import com.xxl.job.client.task.ScheduledTaskThread;

/**
 * @author sweeter
 * @date 2022/12/10
 */
public interface ScheduledThreadRepository {

    ScheduledTaskThread save(int taskId, ScheduledTaskThread scheduledTaskThread);

    ScheduledTaskThread save(int taskId, AbstractHandler handler, String removeOldReason);

    ScheduledTaskThread remove(int taskId, String removeOldReason);

    ScheduledTaskThread findOne(int taskId);

    void clear();

    void destroy();

}

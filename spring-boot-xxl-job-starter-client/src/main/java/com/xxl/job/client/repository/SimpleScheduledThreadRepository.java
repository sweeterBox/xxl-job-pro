package com.xxl.job.client.repository;

import com.xxl.job.client.handler.AbstractHandler;
import com.xxl.job.client.task.ScheduledTaskThread;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author sweeter
 * @date 2022/12/10
 */
@Slf4j
public class SimpleScheduledThreadRepository implements ScheduledThreadRepository {

    private static volatile ConcurrentMap<Integer, ScheduledTaskThread> threadStorage = new ConcurrentHashMap<>();

    @Override
    public ScheduledTaskThread save(int taskId, ScheduledTaskThread scheduledTaskThread) {
        ScheduledTaskThread oldJobThread = threadStorage.put(taskId, scheduledTaskThread);
        if (oldJobThread != null) {
            oldJobThread.toStop("change jobhandler or glue type, and terminate the old job thread.");
            oldJobThread.interrupt();
        }
        return scheduledTaskThread;
    }

    @Override
    public ScheduledTaskThread save(int taskId, AbstractHandler handler, String removeOldReason){
        ScheduledTaskThread newScheduledTaskThread = new ScheduledTaskThread(taskId, handler, this);
        newScheduledTaskThread.start();
        ScheduledTaskThread oldScheduledTaskThread = threadStorage.put(taskId, newScheduledTaskThread);
        if (oldScheduledTaskThread != null) {
            oldScheduledTaskThread.toStop(removeOldReason);
            oldScheduledTaskThread.interrupt();
        }
        return newScheduledTaskThread;
    }

    @Override
    public ScheduledTaskThread remove(int taskId, String removeOldReason) {
        ScheduledTaskThread oldScheduledTaskThread = threadStorage.remove(taskId);
        if (oldScheduledTaskThread != null) {
            oldScheduledTaskThread.toStop(removeOldReason);
            oldScheduledTaskThread.interrupt();
            return oldScheduledTaskThread;
        }
        return null;
    }

    @Override
    public ScheduledTaskThread findOne(int taskId) {
        return threadStorage.get(taskId);
    }

    @Override
    public void clear() {
        threadStorage.clear();

    }

    @Override
    public void destroy() {
        if (threadStorage.size() > 0) {
            for (Map.Entry<Integer, ScheduledTaskThread> item: threadStorage.entrySet()) {
                ScheduledTaskThread oldJobThread = remove(item.getKey(), "web container destroy and kill the job.");
                // wait for job thread push result to callback queue
                if (oldJobThread != null) {
                    try {
                        oldJobThread.join();
                    } catch (InterruptedException e) {
                        log.error(" xxl-job, JobThread destroy(join) error, taskId:{}", item.getKey(), e);
                    }
                }
            }
            this.clear();
        }
    }
}

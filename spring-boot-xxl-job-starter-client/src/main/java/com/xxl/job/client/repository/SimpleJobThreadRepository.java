package com.xxl.job.client.repository;

import com.xxl.job.client.handler.AbstractJobHandler;
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
public class SimpleJobThreadRepository implements JobThreadRepository {

    private static volatile ConcurrentMap<Integer, ScheduledTaskThread> jobThreadStorage = new ConcurrentHashMap<>();

    @Override
    public ScheduledTaskThread save(int jobId, ScheduledTaskThread jobThread) {
        ScheduledTaskThread oldJobThread = jobThreadStorage.put(jobId, jobThread);
        if (oldJobThread != null) {
            oldJobThread.toStop("change jobhandler or glue type, and terminate the old job thread.");
            oldJobThread.interrupt();
        }
        return jobThread;
    }

    @Override
    public ScheduledTaskThread save(int jobId, AbstractJobHandler handler, String removeOldReason){
        ScheduledTaskThread newJobThread = new ScheduledTaskThread(jobId, handler, this);
        newJobThread.start();
        ScheduledTaskThread oldJobThread = jobThreadStorage.put(jobId, newJobThread);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
        }
        return newJobThread;
    }

    @Override
    public ScheduledTaskThread remove(int jobId, String removeOldReason) {
        ScheduledTaskThread oldJobThread = jobThreadStorage.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop(removeOldReason);
            oldJobThread.interrupt();
            return oldJobThread;
        }
        return null;
    }

    @Override
    public ScheduledTaskThread findOne(int jobId) {
        return jobThreadStorage.get(jobId);
    }

    @Override
    public void clear() {
        jobThreadStorage.clear();

    }

    @Override
    public void destroy() {
        if (jobThreadStorage.size() > 0) {
            for (Map.Entry<Integer, ScheduledTaskThread> item: jobThreadStorage.entrySet()) {
                ScheduledTaskThread oldJobThread = remove(item.getKey(), "web container destroy and kill the job.");
                // wait for job thread push result to callback queue
                if (oldJobThread != null) {
                    try {
                        oldJobThread.join();
                    } catch (InterruptedException e) {
                        log.error(" xxl-job, JobThread destroy(join) error, jobId:{}", item.getKey(), e);
                    }
                }
            }
            this.clear();
        }
    }
}

package com.xxl.job.client.executor.impl;

import com.xxl.job.client.executor.model.IdleBeatParam;
import com.xxl.job.client.executor.model.KillParam;
import com.xxl.job.client.executor.model.LogParam;
import com.xxl.job.client.executor.model.TriggerParam;
import com.xxl.job.client.handler.AbstractJobHandler;
import com.xxl.job.client.handler.GlueJobHandler;
import com.xxl.job.client.handler.MethodJobHandler;
import com.xxl.job.client.handler.ScriptJobHandler;
import com.xxl.job.client.enums.BlockStrategy;
import com.xxl.job.client.executor.Executor;
import com.xxl.job.client.glue.GlueFactory;
import com.xxl.job.client.glue.GlueTypeEnum;
import com.xxl.job.client.log.XxlJobFileAppender;
import com.xxl.job.client.repository.JobHandlerRepository;
import com.xxl.job.client.repository.JobThreadRepository;
import com.xxl.job.client.task.ScheduledTaskThread;
import com.xxl.job.model.R;
import com.xxl.job.client.executor.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by xuxueli on 17/3/1.
 */
public class DefaultExecutor implements Executor {

    private static Logger log = LoggerFactory.getLogger(DefaultExecutor.class);

    private JobHandlerRepository jobHandlerRepository;

    private JobThreadRepository jobThreadRepository;

    public DefaultExecutor(JobHandlerRepository jobHandlerRepository, JobThreadRepository jobThreadRepository) {
        this.jobHandlerRepository = jobHandlerRepository;
        this.jobThreadRepository = jobThreadRepository;
    }

    @Override
    public R<String> beat() {
        return R.SUCCESS;
    }

    @Override
    public R<String> idleBeat(IdleBeatParam idleBeatParam) {
        // isRunningOrHasQueue
        boolean isRunningOrHasQueue = false;
        ScheduledTaskThread jobThread = this.jobThreadRepository.findOne(idleBeatParam.getJobId());
        if (jobThread != null && jobThread.isRunningOrHasQueue()) {
            isRunningOrHasQueue = true;
        }

        if (isRunningOrHasQueue) {
            return new R<>(R.FAIL_CODE, "job thread is running or has trigger queue.");
        }
        return R.SUCCESS;
    }

    @Override
    public R<String> run(TriggerParam triggerParam) {
        // load old：jobHandler + jobThread
        ScheduledTaskThread jobThread = this.jobThreadRepository.findOne(triggerParam.getJobId());
        AbstractJobHandler jobHandler = jobThread != null ? jobThread.getHandler() : null;
        String removeOldReason = null;

        // valid：jobHandler + jobThread
        GlueTypeEnum glueTypeEnum = GlueTypeEnum.match(triggerParam.getGlueType());
        if (Objects.isNull(glueTypeEnum)) {
            return new R<>(R.FAIL_CODE, "glueType[" + triggerParam.getGlueType() + "] is not valid.");
        }
        switch (glueTypeEnum) {
            case BEAN:{
                // new jobhandler
                AbstractJobHandler newJobHandler = this.jobHandlerRepository.findOne(triggerParam.getExecutorHandler());

                // valid old jobThread
                if (jobThread!=null && jobHandler != newJobHandler) {
                    // change handler, need kill old thread
                    removeOldReason = "change jobhandler or glue type, and terminate the old job thread.";

                    jobThread = null;
                    jobHandler = null;
                }

                // valid handler
                if (jobHandler == null) {
                    jobHandler = newJobHandler;
                    if (jobHandler == null) {
                        return new R<>(R.FAIL_CODE, "job handler [" + triggerParam.getExecutorHandler() + "] not found.");
                    }
                }
                break;
            }
            case GLUE_GROOVY:{
                // valid old jobThread
                if (jobThread != null &&
                        !(jobThread.getHandler() instanceof GlueJobHandler
                                && ((GlueJobHandler) jobThread.getHandler()).getGlueUpdatetime()==triggerParam.getGlueUpdatetime() )) {
                    // change handler or gluesource updated, need kill old thread
                    removeOldReason = "change job source or glue type, and terminate the old job thread.";

                    jobThread = null;
                    jobHandler = null;
                }

                // valid handler
                if (jobHandler == null) {
                    try {
                        AbstractJobHandler originJobHandler = GlueFactory.getInstance().loadNewInstance(triggerParam.getGlueSource());
                        jobHandler = new GlueJobHandler(originJobHandler, triggerParam.getGlueUpdatetime());
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        return new R<>(R.FAIL_CODE, e.getMessage());
                    }
                }
                break;
            }
            case GLUE_SHELL:
            case GLUE_PHP:
            case GLUE_NODEJS:
            case GLUE_PYTHON:
            case GLUE_POWERSHELL:{
                if (jobThread != null &&
                        !(jobThread.getHandler() instanceof ScriptJobHandler
                                && ((ScriptJobHandler) jobThread.getHandler()).getGlueUpdatetime()==triggerParam.getGlueUpdatetime() )) {
                    // change script or gluesource updated, need kill old thread
                    removeOldReason = "change job source or glue type, and terminate the old job thread.";

                    jobThread = null;
                    jobHandler = null;
                }

                // valid handler
                if (jobHandler == null) {
                    jobHandler = new ScriptJobHandler(triggerParam.getJobId(), triggerParam.getGlueUpdatetime(), triggerParam.getGlueSource(), GlueTypeEnum.match(triggerParam.getGlueType()));
                }

                break;
            }
            default:{
                return new R<>(R.FAIL_CODE, "glueType[" + triggerParam.getGlueType() + "] is not valid.");
            }
        }


        // executor block strategy
        if (jobThread != null) {
            BlockStrategy blockStrategy = BlockStrategy.match(triggerParam.getExecutorBlockStrategy(), null);
            if (Objects.nonNull(blockStrategy)) {
                switch (blockStrategy) {
                    case DISCARD_LATER:{
                        // discard when running
                        if (jobThread.isRunningOrHasQueue()) {
                            return new R<>(R.FAIL_CODE, "block strategy effect："+ BlockStrategy.DISCARD_LATER.getTitle());
                        }
                        break;
                    }
                    case COVER_EARLY:{
                        // kill running jobThread
                        if (jobThread.isRunningOrHasQueue()) {
                            removeOldReason = "block strategy effect：" + BlockStrategy.COVER_EARLY.getTitle();
                            jobThread = null;
                        }
                        break;
                    }
                    default:{
                        // just queue trigger
                    }
                }
            }

        }
        // replace thread (new or exists invalid)
        if (jobThread == null) {
            jobThread = this.jobThreadRepository.save(triggerParam.getJobId(), jobHandler, removeOldReason);
        }
        // push data to queue
        R<String> pushResult = jobThread.pushTriggerQueue(triggerParam);
        return pushResult;
    }

    @Override
    public R<String> kill(KillParam killParam) {
        // kill handlerThread, and create new one
        ScheduledTaskThread jobThread = this.jobThreadRepository.findOne(killParam.getJobId().intValue());
        if (jobThread != null) {
            this.jobThreadRepository.remove(killParam.getJobId().intValue(), "scheduling center kill job.");
            return R.SUCCESS;
        }

        return new R<>(R.SUCCESS_CODE, "job thread already killed.");
    }

    @Override
    public R<LogResult> log(LogParam logParam) {
        // log filename: logPath/yyyy-MM-dd/9999.log
        String logFileName = XxlJobFileAppender.makeLogFileName(new Date(logParam.getLogDateTim()), logParam.getLogId());
        LogResult logResult = XxlJobFileAppender.readLog(logFileName, logParam.getFromLineNum());
        return new R<>(logResult);
    }

    @Override
    public R<List<TaskInfo>> tasks() {
        List<TaskInfo> tasks = new ArrayList<>();
        Collection<AbstractJobHandler> handlers = this.jobHandlerRepository.findAll();
        for (AbstractJobHandler handler : handlers) {
            if (handler instanceof MethodJobHandler) {
                MethodJobHandler methodJobHandler = (MethodJobHandler) handler;
                TaskInfo task = new TaskInfo();
                task.setName(methodJobHandler.getName());
                task.setDescription(methodJobHandler.getDescription());
                task.setDeprecated(methodJobHandler.isDeprecated());
                tasks.add(task);
            }
        }
        return new R<>(tasks);
    }

}

package com.xxl.job.client.executor.impl;

import com.xxl.job.client.executor.model.IdleBeatParam;
import com.xxl.job.client.executor.model.KillParam;
import com.xxl.job.client.executor.model.LogParam;
import com.xxl.job.client.executor.model.TriggerParam;
import com.xxl.job.client.handler.*;
import com.xxl.job.client.enums.BlockStrategy;
import com.xxl.job.client.executor.Executor;
import com.xxl.job.client.glue.GlueFactory;
import com.xxl.job.client.glue.GlueTypeEnum;
import com.xxl.job.client.log.XxlJobFileAppender;
import com.xxl.job.client.repository.ScheduledHandlerRepository;
import com.xxl.job.client.repository.ScheduledThreadRepository;
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

    private ScheduledHandlerRepository scheduledHandlerRepository;

    private ScheduledThreadRepository scheduledThreadRepository;

    public DefaultExecutor(ScheduledHandlerRepository scheduledHandlerRepository, ScheduledThreadRepository scheduledThreadRepository) {
        this.scheduledHandlerRepository = scheduledHandlerRepository;
        this.scheduledThreadRepository = scheduledThreadRepository;
    }

    @Override
    public R<String> beat() {
        return R.SUCCESS;
    }

    @Override
    public R<String> idleBeat(IdleBeatParam idleBeatParam) {
        // isRunningOrHasQueue
        boolean isRunningOrHasQueue = false;
        ScheduledTaskThread jobThread = this.scheduledThreadRepository.findOne(idleBeatParam.getJobId());
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
        ScheduledTaskThread jobThread = this.scheduledThreadRepository.findOne(triggerParam.getJobId());
        AbstractHandler jobHandler = jobThread != null ? jobThread.getHandler() : null;
        String removeOldReason = null;

        // valid：jobHandler + jobThread
        GlueTypeEnum glueTypeEnum = GlueTypeEnum.match(triggerParam.getGlueType());
        if (Objects.isNull(glueTypeEnum)) {
            return new R<>(R.FAIL_CODE, "glueType[" + triggerParam.getGlueType() + "] is not valid.");
        }
        switch (glueTypeEnum) {
            case BEAN:{
                // new jobhandler
                AbstractHandler newHandler = this.scheduledHandlerRepository.findOne(triggerParam.getExecutorHandler());

                // valid old jobThread
                if (jobThread != null && jobHandler != newHandler) {
                    // change handler, need kill old thread
                    removeOldReason = "change jobhandler or glue type, and terminate the old job thread.";
                    jobThread = null;
                    jobHandler = null;
                }

                // valid handler
                if (jobHandler == null) {
                    jobHandler = newHandler;
                    if (jobHandler == null) {
                        return new R<>(R.FAIL_CODE, "job handler [" + triggerParam.getExecutorHandler() + "] not found.");
                    }
                }
                break;
            }
            case GLUE_GROOVY:{
                // valid old jobThread
                if (jobThread != null &&
                        !(jobThread.getHandler() instanceof GlueHandler
                                && ((GlueHandler) jobThread.getHandler()).getGlueUpdatetime()==triggerParam.getGlueUpdatetime() )) {
                    // change handler or gluesource updated, need kill old thread
                    removeOldReason = "change job source or glue type, and terminate the old job thread.";

                    jobThread = null;
                    jobHandler = null;
                }

                // valid handler
                if (jobHandler == null) {
                    try {
                        AbstractHandler originJobHandler = GlueFactory.getInstance().loadNewInstance(triggerParam.getGlueSource());
                        jobHandler = new GlueHandler(originJobHandler, triggerParam.getGlueUpdatetime());
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
                        !(jobThread.getHandler() instanceof ScriptHandler
                                && ((ScriptHandler) jobThread.getHandler()).getGlueUpdatetime()==triggerParam.getGlueUpdatetime() )) {
                    // change script or gluesource updated, need kill old thread
                    removeOldReason = "change job source or glue type, and terminate the old job thread.";

                    jobThread = null;
                    jobHandler = null;
                }

                // valid handler
                if (jobHandler == null) {
                    jobHandler = new ScriptHandler(triggerParam.getJobId(), triggerParam.getGlueUpdatetime(), triggerParam.getGlueSource(), GlueTypeEnum.match(triggerParam.getGlueType()));
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
            jobThread = this.scheduledThreadRepository.save(triggerParam.getJobId(), jobHandler, removeOldReason);
        }
        // push data to queue
        R<String> pushResult = jobThread.pushTriggerQueue(triggerParam);
        return pushResult;
    }

    @Override
    public R<String> kill(KillParam killParam) {
        // kill handlerThread, and create new one
        ScheduledTaskThread jobThread = this.scheduledThreadRepository.findOne(killParam.getJobId().intValue());
        if (jobThread != null) {
            this.scheduledThreadRepository.remove(killParam.getJobId().intValue(), "scheduling center kill job.");
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
        Collection<AbstractHandler> handlers = this.scheduledHandlerRepository.findAll();
        for (AbstractHandler handler : handlers) {
            if (handler instanceof MethodHandler) {
                MethodHandler methodHandler = (MethodHandler) handler;
                TaskInfo task = new TaskInfo();
                task.setName(methodHandler.getName());
                task.setDescription(methodHandler.getDescription());
                task.setDeprecated(methodHandler.isDeprecated());
                tasks.add(task);
            }
            if (handler instanceof BeanHandler) {
                BeanHandler beanHandler = (BeanHandler) handler;
                TaskInfo task = new TaskInfo();
                task.setName(beanHandler.getName());
                task.setDescription(beanHandler.getDescription());
                task.setDeprecated(beanHandler.isDeprecated());
                tasks.add(task);
            }
        }
        return new R<>(tasks);
    }

}

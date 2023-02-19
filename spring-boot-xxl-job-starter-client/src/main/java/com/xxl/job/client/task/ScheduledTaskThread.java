package com.xxl.job.client.task;

import com.xxl.job.client.handler.AbstractHandler;
import com.xxl.job.client.context.XxlJobContext;
import com.xxl.job.client.context.XxlJobHelper;
import com.xxl.job.client.executor.model.HandleCallbackParam;
import com.xxl.job.model.R;
import com.xxl.job.client.executor.model.TriggerParam;
import com.xxl.job.client.log.XxlJobFileAppender;
import com.xxl.job.client.repository.ScheduledThreadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;


/**
 * handler thread
 * @author xuxueli 2016-1-16 19:52:47
 */
public class ScheduledTaskThread extends Thread{

	private static Logger log = LoggerFactory.getLogger(ScheduledTaskThread.class);

	private int jobId;

	private AbstractHandler handler;

	private LinkedBlockingQueue<TriggerParam> triggerQueue;
	// avoid repeat trigger for the same TRIGGER_LOG_ID
	private Set<Long> triggerLogIdSet;

	private volatile boolean toStop = false;

	private String stopReason;

	// if running job
    private boolean running = false;
	// idel times
	private int idleTimes = 0;

	private ScheduledThreadRepository jobThreadRepository;


	public ScheduledTaskThread(int jobId, AbstractHandler handler, ScheduledThreadRepository jobThreadRepository) {
		this.jobId = jobId;
		this.handler = handler;
		this.triggerQueue = new LinkedBlockingQueue<>();
		this.triggerLogIdSet = Collections.synchronizedSet(new HashSet<>());
		// assign job thread name
		this.setName("xxl-job, JobThread-" + jobId + "-" + System.currentTimeMillis());
		this.jobThreadRepository = jobThreadRepository;
	}
	public AbstractHandler getHandler() {
		return handler;
	}

    /**
     * new trigger to queue
     *
     * @param triggerParam
     * @return
     */
	public R<String> pushTriggerQueue(TriggerParam triggerParam) {
		// avoid repeat
		if (triggerLogIdSet.contains(triggerParam.getLogId())) {
			log.info("repeate trigger job, logId:{}", triggerParam.getLogId());
			return new R<>(R.FAIL_CODE, "repeate trigger job, logId:" + triggerParam.getLogId());
		}

		triggerLogIdSet.add(triggerParam.getLogId());
		triggerQueue.add(triggerParam);
        return R.SUCCESS;
	}

    /**
     * kill job thread
     *
     * @param stopReason
     */
	public void toStop(String stopReason) {
		/**
		 * Thread.interrupt只支持终止线程的阻塞状态(wait、join、sleep)，
		 * 在阻塞出抛出InterruptedException异常,但是并不会终止运行的线程本身；
		 * 所以需要注意，此处彻底销毁本线程，需要通过共享变量方式；
		 */
		this.toStop = true;
		this.stopReason = stopReason;
	}

    /**
     * is running job
     * @return
     */
    public boolean isRunningOrHasQueue() {
        return running || triggerQueue.size()>0;
    }

    @Override
	public void run() {

    	// init
    	try {
			handler.init();
		} catch (Throwable e) {
    		log.error(e.getMessage(), e);
		}

		// execute
		while(!toStop){
			running = false;
			idleTimes++;

            TriggerParam triggerParam = null;
			HandleCallbackParam callbackParam = new HandleCallbackParam();
            try {
				// to check toStop signal, we need cycle, so wo cannot use queue.take(), instand of poll(timeout)
				triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
				if (Objects.nonNull(triggerParam)) {
					running = true;
					idleTimes = 0;
					triggerLogIdSet.remove(triggerParam.getLogId());

					// log filename, like "logPath/yyyy-MM-dd/9999.log"
					String logFileName = XxlJobFileAppender.makeLogFileName(new Date(triggerParam.getLogDateTime()), triggerParam.getLogId());
					XxlJobContext xxlJobContext = new XxlJobContext(
							triggerParam.getJobId(),
							triggerParam.getExecutorParams(),
							logFileName,
							triggerParam.getBroadcastIndex(),
							triggerParam.getBroadcastTotal());
					callbackParam.setLogId(triggerParam.getLogId());
					callbackParam.setTimestamp(triggerParam.getLogDateTime());
					// init job context
					XxlJobContext.setXxlJobContext(xxlJobContext);

					// execute
					XxlJobHelper.log("<br>----------- xxl-job job execute start -----------<br>----------- Param:" + xxlJobContext.getJobParam());

					if (triggerParam.getExecutorTimeout() > 0) {
						// limit timeout
						Thread futureThread = null;
						try {
							callbackParam.setStartTime(LocalDateTime.now());
							FutureTask<Boolean> futureTask = new FutureTask<>(() -> {
								// init job context
								XxlJobContext.setXxlJobContext(xxlJobContext);
								handler.execute();
								return true;
							});
							futureThread = new Thread(futureTask);
							futureThread.start();
							Boolean tempResult = futureTask.get(triggerParam.getExecutorTimeout(), TimeUnit.SECONDS);
						} catch (TimeoutException e) {
							XxlJobHelper.log("<br>----------- xxl-job job execute timeout");
							XxlJobHelper.log(e);
							// handle result
							XxlJobHelper.handleTimeout("job execute timeout ");
						} finally {
							callbackParam.setEndTime(LocalDateTime.now());
							futureThread.interrupt();
						}
					} else {
						// just execute
						callbackParam.setStartTime(LocalDateTime.now());
						// 0:执行中
						callbackParam.setStatus(0);
						TriggerCallbackThread.pushCallBack(callbackParam);
						handler.execute();
						callbackParam.setEndTime(LocalDateTime.now());
					}

					// valid execute handle data
					if (XxlJobContext.getXxlJobContext().getHandleCode() <= 0) {
						XxlJobHelper.handleFail("job handle result lost.");
					} else {
						String tempHandleMsg = XxlJobContext.getXxlJobContext().getHandleMsg();
						tempHandleMsg = (tempHandleMsg != null && tempHandleMsg.length() > 50000)
								? tempHandleMsg.substring(0, 50000).concat("...")
								: tempHandleMsg;
						XxlJobContext.getXxlJobContext().setHandleMsg(tempHandleMsg);
					}
					XxlJobHelper.log("<br>----------- xxl-job job execute end(finish) -----------<br>----------- Result: handleCode="
							+ XxlJobContext.getXxlJobContext().getHandleCode()
							+ ", handleMsg = "
							+ XxlJobContext.getXxlJobContext().getHandleMsg()
					);

				} else {
					if (idleTimes > 30) {
						if (triggerQueue.size() == 0) {
							// avoid concurrent trigger causes jobId-lost
							this.jobThreadRepository.remove(jobId, "excutor idel times over limit.");
						}
					}
				}
			} catch (Throwable e) {
				if (toStop) {
					XxlJobHelper.log("<br>----------- JobThread toStop, stopReason:" + stopReason);
				}

				// handle result
				StringWriter stringWriter = new StringWriter();
				e.printStackTrace(new PrintWriter(stringWriter));
				String errorMsg = stringWriter.toString();

				XxlJobHelper.handleFail(errorMsg);

				XxlJobHelper.log("<br>----------- JobThread Exception:" + errorMsg + "<br>----------- xxl-job job execute end(error) -----------");
			} finally {
                if(triggerParam != null) {
                    // callback handler info
					if (Objects.isNull(callbackParam.getEndTime())) {
						callbackParam.setEndTime(LocalDateTime.now());
					}
                    if (!toStop) {
                        // commonm
						callbackParam.setStatus(XxlJobContext.getXxlJobContext().getHandleCode());
						callbackParam.setContent(XxlJobContext.getXxlJobContext().getHandleMsg());

                        TriggerCallbackThread.pushCallBack(callbackParam);
                    } else {
                        // is killed
						callbackParam.setStatus(XxlJobContext.HANDLE_CODE_FAIL);
						callbackParam.setContent(stopReason);
                        TriggerCallbackThread.pushCallBack(callbackParam);
                    }
                }
            }
        }

		// callback trigger request in queue
		while(triggerQueue !=null && triggerQueue.size()>0){
			TriggerParam triggerParam = triggerQueue.poll();
			if (triggerParam!=null) {
				// is killed
				HandleCallbackParam callbackParam = new HandleCallbackParam();
/*				HandleCallbackParam callbackParam = new HandleCallbackParam(triggerParam.getLogId(), XxlJobContext.HANDLE_CODE_FAIL
						, stopReason + " [job not executed, in the job queue, killed.]"
						, null
						, null
						, triggerParam.getLogDateTime()
				);*/
				callbackParam.setLogId(triggerParam.getLogId());
				callbackParam.setStatus(XxlJobContext.HANDLE_CODE_FAIL);
				callbackParam.setContent(stopReason + " [job not executed, in the job queue, killed.]");
				callbackParam.setTimestamp(triggerParam.getLogDateTime());
				callbackParam.setMetadata(null);

				TriggerCallbackThread.pushCallBack(callbackParam);
			}
		}
		// destroy
		try {
			handler.destroy();
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		}
		log.info("xxl-job JobThread stoped, hashCode:{}", Thread.currentThread());
	}
}

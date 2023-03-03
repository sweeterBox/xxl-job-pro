package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.enums.NotifyStatus;
import com.xxl.job.admin.repository.LogRepository;
import com.xxl.job.utils.SpringContextUtils;
import com.xxl.job.admin.core.complete.XxlJobCompleter;
import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.admin.service.LogService;
import com.xxl.job.model.HandleCallbackParam;
import com.xxl.job.model.R;
import com.xxl.job.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * job lose-monitor instance
 *
 * @author xuxueli 2015-9-1 18:05:56
 */
public class JobCompleteHelper {


	private static Logger logger = LoggerFactory.getLogger(JobCompleteHelper.class);

	private static JobCompleteHelper instance = new JobCompleteHelper();
	public static JobCompleteHelper getInstance(){
		return instance;
	}

	// ---------------------- monitor ----------------------

	private ThreadPoolExecutor callbackThreadPool = null;
	private Thread monitorThread;
	private volatile boolean toStop = false;
	public void start(){

		// for callback
		callbackThreadPool = new ThreadPoolExecutor(
				2,
				20,
				30L,
				TimeUnit.SECONDS,
				new LinkedBlockingQueue<Runnable>(3000),
				r -> new Thread(r, "xxl-job, admin JobLosedMonitorHelper-callbackThreadPool-" + r.hashCode()),
				(r, executor) -> {
					r.run();
					logger.warn(" xxl-job, callback too fast, match threadpool rejected handler(run now).");
				});


		// for monitor
		monitorThread = new Thread(() -> {

			// wait for JobTriggerPoolHelper-init
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				if (!toStop) {
					logger.error(e.getMessage(), e);
				}
			}

			// monitor
			while (!toStop) {
				try {
					//TODO 废弃以下代码，重新实现主动检查任务状态

					// 任务结果丢失处理：调度记录停留在 "运行中" 状态超过10min，且对应执行器心跳注册失败不在线，则将本地调度主动标记失败；
					Date losedTime = DateUtil.addMinutes(new Date(), -10);
					List<Long> losedJobIds  = new ArrayList<>();// SpringContextUtils.getBean(LogRepository.class).findLostJobIds(losedTime);

					if (losedJobIds!=null && losedJobIds.size()>0) {
						for (Long logId: losedJobIds) {
							Log jobLog = SpringContextUtils.getBean(LogRepository.class).getOne(logId);
							jobLog.setHandleEndTime(LocalDateTime.now());
							jobLog.setHandleStatus(R.FAIL_CODE);
							jobLog.setHandleContent( I18nUtil.getString("joblog_lost_fail") );
							jobLog.setNotifyStatus(NotifyStatus.TODO);
							XxlJobCompleter.updateHandleInfoAndFinish(jobLog);
						}

					}
				} catch (Exception e) {
					if (!toStop) {
						logger.error(" xxl-job, job fail monitor thread error:{}", e);
					}
				}

				try {
					   TimeUnit.SECONDS.sleep(60);
				} catch (Exception e) {
					if (!toStop) {
					   logger.error(e.getMessage(), e);
					}
				}
			}
			logger.info(" xxl-job, JobLosedMonitorHelper stop");

		});
		monitorThread.setDaemon(true);
		monitorThread.setName("xxl-job, admin JobLosedMonitorHelper");
		monitorThread.start();
	}

	public void toStop(){
		toStop = true;

		// stop registryOrRemoveThreadPool
		callbackThreadPool.shutdownNow();

		// stop monitorThread (interrupt and wait)
		monitorThread.interrupt();
		try {
			monitorThread.join();
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}


	// ---------------------- helper ----------------------

	public R<String> callback(List<HandleCallbackParam> callbackParamList) {

		callbackThreadPool.execute(() -> {
			for (HandleCallbackParam handleCallbackParam: callbackParamList) {
				R<String> callbackResult = callback(handleCallbackParam);
				logger.debug("ApiController.callback {}, handleCallbackParam={}, callbackResult={}", (callbackResult.getCode()== R.SUCCESS_CODE?"success":"fail"), handleCallbackParam, callbackResult);
			}
		});

		return R.SUCCESS;
	}

	private R<String> callback(HandleCallbackParam param) {
		// valid log item
		LogService logService = SpringContextUtils.getBean(LogService.class);
		Log en = logService.findOne(param.getLogId());
		if (en == null) {
			return new R<>(R.FAIL_CODE, "log item not found.");
		}

		// avoid repeat callback, trigger child job etc
		if (en.getHandleStatus() > 0) {
			return new R<>(R.FAIL_CODE, "en repeate callback.");
		}

		// handle msg
		StringBuffer handleMsg = new StringBuffer();
		if (en.getHandleContent()!=null) {
			handleMsg.append(en.getHandleContent()).append("<br>");
		}
		if (param.getContent() != null) {
			handleMsg.append(param.getContent());
		}

		// success, save log
		if (Objects.isNull(en.getHandleStartTime())) {
			en.setHandleStartTime(param.getStartTime());
		}
		en.setHandleEndTime(param.getEndTime());
		en.setHandleStatus(param.getStatus());
		en.setHandleContent(handleMsg.toString());
		if (param.getStatus().compareTo(200) == 0 || param.getStatus().compareTo(0) == 0) {
			en.setNotifyStatus(NotifyStatus.NOT);
		}else {
			en.setNotifyStatus(NotifyStatus.TODO);
		}
		// text最大64kb 避免长度过长
		if (StringUtils.isNotBlank(en.getHandleContent()) && en.getHandleContent().length() > 15000) {
			en.setHandleContent(en.getHandleContent().substring(0, 15000));
		}
		XxlJobCompleter.updateHandleInfoAndFinish(en);
		return R.SUCCESS;
	}



}

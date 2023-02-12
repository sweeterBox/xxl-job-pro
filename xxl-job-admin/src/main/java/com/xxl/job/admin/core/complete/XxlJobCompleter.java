package com.xxl.job.admin.core.complete;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.entity.Task;
import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.core.thread.TaskTriggerThread;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.core.util.I18nUtil;
import com.xxl.job.model.R;
import com.xxl.job.utils.XxlJobContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @author xuxueli 2020-10-30 20:43:10
 */
public class XxlJobCompleter {
    private static Logger logger = LoggerFactory.getLogger(XxlJobCompleter.class);

    /**
     * common fresh handle entrance (limit only once)
     *
     * @param log
     * @return
     */
    public static void updateHandleInfoAndFinish(Log log) {

        // finish
        finishJob(log);

        // text最大64kb 避免长度过长
        if (log.getHandleContent().length() > 15000) {
            log.setHandleContent( log.getHandleContent().substring(0, 15000) );
        }

        // fresh handle
         XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().save(log);
    }


    /**
     * do somethind to finish job
     */
    private static void finishJob(Log log){

        // 1、handle success, to trigger child job
        String triggerChildMsg = null;
        if (XxlJobContext.HANDLE_CODE_SUCCESS == log.getHandleStatus()) {
            Task info = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().getOne(log.getTaskId());
            if (info !=null && info.getChildJobId()!=null && info.getChildJobId().trim().length()>0) {
                triggerChildMsg = "<br><br><span style=\"color:#00c0ef;\" > "+ I18nUtil.getString("jobconf_trigger_child_run") +"<<<<<<<<<<< </span><br>";

                String[] childJobIds = info.getChildJobId().split(",");
                for (int i = 0; i < childJobIds.length; i++) {
                    int childJobId = (childJobIds[i]!=null && childJobIds[i].trim().length()>0 && isNumeric(childJobIds[i]))?Integer.valueOf(childJobIds[i]):-1;
                    if (childJobId > 0) {

                        TaskTriggerThread.trigger(Long.valueOf(childJobId), TriggerTypeEnum.PARENT, -1, null, null);
                        R<String> triggerChildResult = R.SUCCESS;

                        // add msg
                        triggerChildMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg1"),
                                (i+1),
                                childJobIds.length,
                                childJobIds[i],
                                (triggerChildResult.getCode()== R.SUCCESS_CODE?I18nUtil.getString("system_success"):I18nUtil.getString("system_fail")),
                                triggerChildResult.getMsg());
                    } else {
                        triggerChildMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg2"),
                                (i+1),
                                childJobIds.length,
                                childJobIds[i]);
                    }
                }

            }
        }

        if (triggerChildMsg != null) {
            log.setHandleContent( log.getHandleContent() + triggerChildMsg );
        }

        // 2、fix_delay trigger next
        // on the way

    }

    private static boolean isNumeric(String str){
        try {
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}

package com.xxl.job.admin.core;

import com.xxl.job.model.*;

import java.util.List;

/**
 * Created by xuxueli on 17/3/1.
 */
public interface Executor {

    /**
     * beat
     * @return
     */
    R<String> beat();

    /**
     * idle beat
     * 闲置
     *
     * @param idleBeatParam
     * @return
     */
    R<String> idleBeat(IdleBeatParam idleBeatParam);

    /**
     * run
     * @param triggerParam
     * @return
     */
    R<String> run(TriggerParam triggerParam);

    /**
     * kill
     * @param killParam
     * @return
     */
    R<String> kill(KillParam killParam);

    /**
     * log
     * @param logParam
     * @return
     */
    R<LogResult> log(LogParam logParam);

    R<List<TaskInfo>> tasks();

}

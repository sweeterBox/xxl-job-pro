package com.xxl.job.admin.notify;

import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.entity.Task;
import lombok.Data;

/**
 * @author sweeter
 * @date 2023/1/20
 */
@Data
public class TaskEvent extends Event {

    private Task task;

    private Instance instance;

    private Log log;

    private Boolean success;

}

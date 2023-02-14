package com.xxl.job.admin.notify;

import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.entity.Task;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author sweeter
 * @date 2023/1/20
 */
@Setter
@Getter
public class TaskEvent extends Event {

    private Task task;

   // private Instance instance;

    private Log log;

   // private Boolean success;

}

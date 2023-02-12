package com.xxl.job.admin.query;

import com.xxl.job.admin.common.ReqPage;
import com.xxl.job.admin.common.jpa.query.Query;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author sweeter
 * @date 2022/9/3
 */
@Data
public class LogQuery extends ReqPage {

    @Query(type = Query.Type.EQUAL)
    private String applicationName;

    @Query(type = Query.Type.EQUAL)
    private String taskId;

    @Query(type = Query.Type.BETWEEN)
    private List<String> triggerTime;

    @Query(type = Query.Type.EQUAL)
    private String triggerStatus;

    @Query(type = Query.Type.EQUAL)
    private String handleStatus;
}

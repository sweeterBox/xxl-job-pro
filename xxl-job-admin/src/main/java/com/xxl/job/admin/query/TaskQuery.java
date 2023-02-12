package com.xxl.job.admin.query;

import com.xxl.job.admin.common.ReqPage;
import com.xxl.job.admin.common.jpa.query.Query;
import lombok.Data;

/**
 * @author sweeter
 * @date 2022/9/3
 */
@Data
public class TaskQuery extends ReqPage {

    @Query(type = Query.Type.EQUAL)
    private String applicationName;

    @Query(type = Query.Type.INNER_LIKE)
    private String description;

    @Query(type = Query.Type.INNER_LIKE)
    private String author;

    @Query(type = Query.Type.INNER_LIKE)
    private String executorHandler;

    @Query(type = Query.Type.EQUAL)
    private Integer triggerStatus;

}

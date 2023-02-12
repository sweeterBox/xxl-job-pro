package com.xxl.job.admin.query;

import com.xxl.job.admin.common.ReqPage;
import com.xxl.job.admin.common.jpa.query.Query;
import lombok.Data;

/**
 * @author sweeter
 * @date 2023/2/5
 */
@Data
public class InstanceQuery extends ReqPage {

    @Query(type = Query.Type.EQUAL)
    private String name;

    @Query(type = Query.Type.INNER_LIKE)
    private String title;

}

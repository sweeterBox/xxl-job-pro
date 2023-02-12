package com.xxl.job.admin.query;

import com.xxl.job.admin.common.ReqPage;
import com.xxl.job.admin.common.jpa.query.Query;
import lombok.Data;

/**
 * @author sweeter
 * @date 2022/9/3
 */
@Data
public class ApplicationQuery extends ReqPage {

    @Query(type = Query.Type.INNER_LIKE)
    private String name;

    @Query(type = Query.Type.INNER_LIKE)
    private String title;
}

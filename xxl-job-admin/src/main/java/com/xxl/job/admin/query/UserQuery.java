package com.xxl.job.admin.query;

import com.xxl.job.admin.common.ReqPage;
import com.xxl.job.admin.common.jpa.query.Query;
import lombok.Data;

/**
 * @author sweeter
 * @description
 * @date 2022/9/3
 */
@Data
public class UserQuery extends ReqPage {

    @Query(type = Query.Type.INNER_LIKE)
    private String username;

    @Query(type = Query.Type.EQUAL)
    private Integer role;


}

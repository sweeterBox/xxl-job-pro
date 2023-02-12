package com.xxl.job.admin.model;

import lombok.Data;

/**
 * @author sweeter
 * @date 2022/9/4
 */
@Data
public class ReqLogin {

    private String userName;

    private String password;

    private Boolean remember;

}

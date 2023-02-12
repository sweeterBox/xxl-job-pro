package com.xxl.job.admin.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author sweeter
 * @date 2023/2/10
 */
@Data
public class ReqExeOnce {

    @NotNull
    private Long id;

    private String clientUrl;

    private String executorParam;
}

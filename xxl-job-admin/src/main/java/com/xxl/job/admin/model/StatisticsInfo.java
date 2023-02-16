package com.xxl.job.admin.model;

import lombok.Data;

/**
 * @author sweeter
 * @date 2023/2/16
 */
@Data
public class StatisticsInfo {

    private Long taskAllNum;

    private Long taskRunningNum;

    private Long triggerAllNum;

    private Long triggerSuccessNum;

    private Long instanceAllNum;

    private Long instanceUpNum;

}

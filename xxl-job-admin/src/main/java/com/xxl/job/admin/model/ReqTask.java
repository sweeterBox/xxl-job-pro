package com.xxl.job.admin.model;

import com.xxl.job.admin.enums.GlueType;
import com.xxl.job.admin.enums.MisfireStrategy;
import com.xxl.job.admin.enums.RouteStrategy;
import com.xxl.job.admin.enums.ScheduleType;
import com.xxl.job.enums.ExecutorBlockStrategy;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotBlank;

/**
 * @author sweeter
 * @date 2023/2/5
 */
@Data
public class ReqTask {

    private Long id;

    @NotBlank
    private String applicationName;

    @NotBlank
    private String description;

    private String author;

    private String alarmEmail;

    private ScheduleType scheduleType;

    private String scheduleConf;

    private GlueType glueType;

    private String executorHandler;

    private String executorParam;

    private RouteStrategy executorRouteStrategy;

    private String childJobId;

    private MisfireStrategy misfireStrategy;

    private String executorBlockStrategy;

    private Long executorTimeout;

    private Integer executorFailRetryCount;

    public void validate() {
        if (GlueType.BEAN.equals(glueType)) {
            if (StringUtils.isBlank(this.executorHandler)) {
                throw new RuntimeException("executorHandler is null");
            }

        }
    }


}

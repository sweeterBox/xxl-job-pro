package com.xxl.job.admin.service;

import java.util.Date;
import java.util.Map;

/**
 * @author sweeter
 * @date 2022/12/24
 */
public interface DashboardService {

    Map<String, Object> chart(Date startDate, Date endDate);

    Map<String, Object> statistics();

}

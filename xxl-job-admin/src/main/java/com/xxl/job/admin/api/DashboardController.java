package com.xxl.job.admin.api;

import com.xxl.job.admin.model.StatisticsInfo;
import com.xxl.job.admin.model.SystemInfo;
import com.xxl.job.admin.service.DashboardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;


/**
 * @author sweeter
 * @description 数据看板
 * @date 2022/9/10
 */
@ControllerAdvice
@Api(tags = "看板(Dashboard)")
@RestController
@RequestMapping("/v1.0/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @ApiOperation("统计(chart)")
    @GetMapping(value = "chart")
    public ResponseEntity<Map<String, Object>> chart(Date startDate, Date endDate) {
       Map<String, Object> data = dashboardService.chart(startDate, endDate);
        return ResponseEntity.ok(data);
    }

    @ApiOperation("统计(card)")
    @GetMapping(value = "statistics")
    public ResponseEntity<StatisticsInfo> statistics() {
        StatisticsInfo info = dashboardService.statistics();
        return ResponseEntity.ok(info);
    }

    @ApiOperation("system Info")
    @GetMapping(value = "systemInfo")
    public ResponseEntity<SystemInfo> systemInfo() {
        SystemInfo systemInfo = dashboardService.findSystemInfo();
        return ResponseEntity.ok(systemInfo);
    }



}

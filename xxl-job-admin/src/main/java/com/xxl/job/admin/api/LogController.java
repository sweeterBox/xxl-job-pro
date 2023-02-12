package com.xxl.job.admin.api;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.query.LogQuery;
import com.xxl.job.admin.service.LogService;
import com.xxl.job.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

/**
 * @author sweeter
 * @description 日志相关
 * @date 2022/9/11
 */
@Api(tags = "日志(Log)")
@RestController
@RequestMapping("/v1.0/log")
public class LogController {

    @Autowired
    private LogService logService;

    @ApiOperation(value = "查询列表")
    @GetMapping("findPageList")
    public ResponseEntity<ResultPage<Log>> findPageList(LogQuery query) {
        return new ResponseEntity<>(logService.findPageList(query), HttpStatus.OK);
    }

    @ApiOperation(value = "查询详情")
    @GetMapping("/{id}")
    public ResponseEntity<Log> findOne(@PathVariable("id") Long id) {
        Log en = logService.findOne(id);
        return new ResponseEntity<>(en, HttpStatus.OK);
    }

    @GetMapping("clear")
    public ResponseEntity<Void> clearLog(String applicationName, Long taskId, Integer type) {
        Date clearBeforeTime = null;
        int clearBeforeNum = 0;
        if (type == 1) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -1);    // 清理一个月之前日志数据
        } else if (type == 2) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -3);    // 清理三个月之前日志数据
        } else if (type == 3) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -6);    // 清理六个月之前日志数据
        } else if (type == 4) {
            clearBeforeTime = DateUtil.addYears(new Date(), -1);    // 清理一年之前日志数据
        } else if (type == 5) {
            clearBeforeNum = 1000;        // 清理一千条以前日志数据
        } else if (type == 6) {
            clearBeforeNum = 10000;        // 清理一万条以前日志数据
        } else if (type == 7) {
            clearBeforeNum = 30000;        // 清理三万条以前日志数据
        } else if (type == 8) {
            clearBeforeNum = 100000;    // 清理十万条以前日志数据
        } else if (type == 9) {
            clearBeforeNum = 0;            // 清理所有日志数据
        } else {

        }
        //TODO 实现日志清理功能

        return ResponseEntity.ok(null);
    }

}

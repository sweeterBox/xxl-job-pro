package com.xxl.job.admin.api;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.Application;
import com.xxl.job.admin.model.ApplicationInfo;
import com.xxl.job.admin.query.ApplicationQuery;
import com.xxl.job.admin.service.ApplicationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author sweeter
 * @description
 * @date 2022/9/4
 */
@Api(tags = "应用(Application)")
@RestController
@RequestMapping("/v1.0/application")
public class ApplicationController {

    @Resource
    private ApplicationService applicationService;


    @ApiOperation(value = "查询列表")
    @GetMapping(value = "findPageList")
    public ResponseEntity<ResultPage<ApplicationInfo>> findPageList(ApplicationQuery query) {
        return new ResponseEntity<>(applicationService.findPageList(query), HttpStatus.OK);
    }


    @ApiOperation(value = "新增")
    @PostMapping()
    public ResponseEntity<Void> save(Application application) {
        applicationService.save(application);
        return ResponseEntity.ok(null);
    }


    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicationService.delete(id);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "查询详情")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ApplicationInfo> info(@PathVariable Long id) {
        ApplicationInfo application = applicationService.findOne(id);
        return ResponseEntity.ok(application);
    }

    @ApiOperation(value = "查询所有")
    @GetMapping(value = "/findAll")
    public ResponseEntity<List<Application>> findAll() {
        List<Application> applications = applicationService.findAll();
        return ResponseEntity.ok(applications);
    }
}

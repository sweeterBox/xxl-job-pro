package com.xxl.job.admin.api;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.Instance;
import com.xxl.job.admin.model.ApplicationInfo;
import com.xxl.job.admin.query.ApplicationQuery;
import com.xxl.job.admin.query.InstanceQuery;
import com.xxl.job.admin.service.InstanceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @author sweeter
 * @date 2022/12/3
 */
@Api(tags = "应用实例(Instance)")
@RestController
@RequestMapping("/v1.0/instance")
public class InstanceController {

    @Autowired
    private InstanceService instanceService;

    @ApiOperation("查询所有应用实例")
    @GetMapping("all")
    public ResponseEntity<List<Instance>> all(String applicationName) {
        List<Instance> all = instanceService.findAll(applicationName);
        return ResponseEntity.ok(all);
    }


    @ApiOperation(value = "查询列表")
    @GetMapping(value = "findPageList")
    public ResponseEntity<ResultPage<Instance>> findPageList(InstanceQuery query) {
        return new ResponseEntity<>(instanceService.findPageList(query), HttpStatus.OK);
    }



}

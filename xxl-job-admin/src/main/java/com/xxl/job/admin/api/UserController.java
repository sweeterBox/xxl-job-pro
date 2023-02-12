package com.xxl.job.admin.api;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.entity.User;
import com.xxl.job.admin.query.UserQuery;
import com.xxl.job.admin.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


/**
 * @author sweeter
 * @date 2022/9/4
 */
@Api(tags = "用户(User)")
@RestController
@RequestMapping("/v1.0/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "查询列表(Query List)")
    @GetMapping("findPageList")
    public ResponseEntity<ResultPage<User>> findPageList(UserQuery query) {
        ResultPage<User> resultPage = userService.findPageList(query);
        return ResponseEntity.ok(resultPage);
    }


    @ApiOperation(value = "保存(Save)")
    @PostMapping()
    public ResponseEntity<Void> save(User user) {
        userService.save(user);
        return ResponseEntity.ok(null);
    }


    @ApiOperation(value = "修改当前用户密码(Change password )",notes = "Change the password of the current user")
    @PutMapping("/changePwd")
    public ResponseEntity<Void> changePwd(String password) {
        userService.changePwd(password);
        return ResponseEntity.ok(null);
    }

    @ApiOperation(value = "删除(Delete)")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(null);
    }


}

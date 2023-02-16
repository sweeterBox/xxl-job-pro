package com.xxl.job.admin.notify;

import com.xxl.job.admin.permission.Permission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sweeter
 * @date 2023/2/15
 */
@Slf4j
@RequestMapping("webhook")
@RestController
public class WebhookController {

    @Permission(limit = false)
    @PostMapping("test")
    public void test(@RequestBody String param) {
        log.error("webhook回调通知测试 入参{}", param);
    }
}

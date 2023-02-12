package com.xxl.job.admin.client;

import com.xxl.job.admin.permission.Permission;
import com.xxl.job.admin.service.impl.ApiServiceImpl;
import com.xxl.job.model.HandleCallbackParam;
import com.xxl.job.model.InstanceRegistry;
import com.xxl.job.model.R;
import com.xxl.job.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author sweeter
 * @date 2023/01/25
 */
@Slf4j
@Controller
@RequestMapping("api")
public class ApiController {

    @Resource
    private ApiServiceImpl apiService;

    /**
     * client callback mapping
     *
     * @param type 类型
     * @param data 入参数据
     * @return
     */
    @PostMapping("/{type}")
    @ResponseBody
    @Permission(limit=false)
    public R<String> api(HttpServletRequest request, @PathVariable("type") String type, @RequestBody(required = false) String data) {
        log.info("api/{} 客户端请求入参信息：{}", type,data);
        ServiceType serviceType = ServiceType.of(type);
        switch (serviceType) {
            case CALLBACK:{
                List<HandleCallbackParam> callbackParamList = JsonUtils.json2Obj(data, List.class, HandleCallbackParam.class);
                return apiService.callback(callbackParamList);
            }
            case REGISTRY:{
                InstanceRegistry registryParam = JsonUtils.json2Obj(data, InstanceRegistry.class);
                return apiService.registry(registryParam);
            }
            case DEREGISTER:{
                InstanceRegistry registryParam = JsonUtils.json2Obj(data, InstanceRegistry.class);
                return apiService.deregister(registryParam);
            }
            default:{
                return new R<>(R.FAIL_CODE, "invalid request, uri-mapping(" + type + ") not found.");
            }
        }
    }

}

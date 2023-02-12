package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.service.LogGlueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author sweeter
 * @date 2022/9/3
 */
@Transactional(rollbackFor = Exception.class,readOnly = true)
@Slf4j
@Service
public class LogGlueServiceImpl implements LogGlueService {

}

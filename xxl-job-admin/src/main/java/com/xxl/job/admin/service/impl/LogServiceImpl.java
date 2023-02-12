package com.xxl.job.admin.service.impl;

import com.xxl.job.admin.common.ResultPage;
import com.xxl.job.admin.common.jpa.query.QueryHandler;
import com.xxl.job.admin.entity.Log;
import com.xxl.job.admin.query.LogQuery;
import com.xxl.job.admin.repository.LogRepository;
import com.xxl.job.admin.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * @author sweeter
 * @date 2022/9/3
 */
@Transactional(rollbackFor = Exception.class,readOnly = true)
@Slf4j
@Service
public class LogServiceImpl  implements LogService {

    @Resource
    private LogRepository logRepository;

    @Transactional(readOnly = false)
    @Override
    public int updateAlarmStatus(long failLogId, int i, int i1) {
        return this.logRepository.updateAlarmStatus(failLogId, i, i1);
    }

    @Override
    public ResultPage<Log> findPageList(LogQuery query) {
        PageRequest pageRequest = query.createPageRequest(Sort.by(Sort.Direction.DESC, "id"));
        Page<Log> page = this.logRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHandler.getPredicate(root, query, criteriaBuilder),pageRequest);
        return ResultPage.of(page);
    }

    @Override
    public Log findOne(Long id) {
        return this.logRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = false)
    @Override
    public void save(Log entity) {
        this.logRepository.save(entity);
    }
}

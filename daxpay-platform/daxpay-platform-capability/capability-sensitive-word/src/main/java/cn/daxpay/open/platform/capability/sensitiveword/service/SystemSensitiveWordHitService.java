package cn.daxpay.open.platform.capability.sensitiveword.service;

import cn.daxpay.open.platform.capability.sensitiveword.dao.SystemSensitiveWordHitManager;
import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordHitQuery;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/// # 敏感词命中记录服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSensitiveWordHitService {

    private final SystemSensitiveWordHitManager systemSensitiveWordHitManager;

    /// 分页
    public PageResult<SystemSensitiveWordHitResult> page(PageParam pageParam, SystemSensitiveWordHitQuery query) {
        return MpUtil.toPageResult(systemSensitiveWordHitManager.page(pageParam, query));
    }

    /// 详情
    public SystemSensitiveWordHitResult findById(Long id) {
        return systemSensitiveWordHitManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.common.sensitiveWordHitNotFound"))
                .toResult();
    }

    /// 写入命中
    @Transactional(rollbackFor = Exception.class)
    public void record(SystemSensitiveWordHit hit) {
        systemSensitiveWordHitManager.save(hit);
    }
}


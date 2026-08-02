package cn.daxpay.open.platform.capability.sensitiveword.service;

import cn.daxpay.open.platform.capability.sensitiveword.dao.SystemSensitiveWordHitManager;
import cn.daxpay.open.platform.capability.sensitiveword.entity.SystemSensitiveWordHit;
import cn.daxpay.open.platform.capability.sensitiveword.param.SystemSensitiveWordHitQuery;
import cn.daxpay.open.platform.capability.sensitiveword.result.SystemSensitiveWordHitResult;
import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.rest.param.PageParam;
import cn.daxpay.open.platform.core.rest.result.PageResult;
import cn.daxpay.open.platform.core.spi.MerchantNameResolver;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/// # 敏感词命中记录服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSensitiveWordHitService {

    private final SystemSensitiveWordHitManager systemSensitiveWordHitManager;

    // 商户名称解析器(可选, 无 payment 模块时为 null)
    private final ObjectProvider<MerchantNameResolver> merchantNameResolverProvider;

    /// 分页
    public PageResult<SystemSensitiveWordHitResult> page(PageParam pageParam, SystemSensitiveWordHitQuery query) {
        PageResult<SystemSensitiveWordHitResult> pageResult = MpUtil.toPageResult(systemSensitiveWordHitManager.page(pageParam, query));
        fillMchName(pageResult.getRecords());
        return pageResult;
    }

    /// 详情
    public SystemSensitiveWordHitResult findById(Long id) {
        SystemSensitiveWordHitResult result = systemSensitiveWordHitManager.findById(id)
                .orElseThrow(() -> new DataNotExistException("error.common.sensitiveWordHitNotFound"))
                .toResult();
        fillMchName(List.of(result));
        return result;
    }

    /// 批量回填商户名称(platform 模块通过 SPI 翻译 mchNo, 无 payment 模块时跳过)
    private void fillMchName(List<SystemSensitiveWordHitResult> list) {
        MerchantNameResolver resolver = merchantNameResolverProvider.getIfAvailable();
        if (resolver == null) {
            return;
        }
        Set<String> mchNos = list.stream()
                .map(SystemSensitiveWordHitResult::getMchNo)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
        if (mchNos.isEmpty()) {
            return;
        }
        Map<String, String> nameMap = resolver.resolveNames(mchNos);
        list.forEach(item -> item.setMchName(nameMap.get(item.getMchNo())));
    }

    /// 写入命中
    @Transactional(rollbackFor = Exception.class)
    public void record(SystemSensitiveWordHit hit) {
        systemSensitiveWordHitManager.save(hit);
    }
}


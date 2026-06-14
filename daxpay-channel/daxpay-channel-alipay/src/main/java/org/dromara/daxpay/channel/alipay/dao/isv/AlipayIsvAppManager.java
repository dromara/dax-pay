package org.dromara.daxpay.channel.alipay.dao.isv;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvApp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/// # 支付宝服务商应用
///
/// 服务商应用数据访问管理器，提供列表查询、按支付宝应用ID查找和唯一性校验等方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAppManager extends BaseManager<AlipayIsvAppMapper, AlipayIsvApp> {

    /// 查询全部应用（按创建时间升序，先创建的在前、新增的排在后面）
    public List<AlipayIsvApp> listAll() {
        return lambdaQuery()
                .orderByAsc(AlipayIsvApp::getCreateTime)
                .orderByAsc(AlipayIsvApp::getId)
                .list();
    }

    /// 根据支付宝应用ID查询
    public Optional<AlipayIsvApp> findByAliAppId(String aliAppId) {
        return Optional.ofNullable(lambdaQuery()
                .eq(AlipayIsvApp::getAliAppId, aliAppId)
                .one());
    }

    /// 校验支付宝应用ID是否已存在(排除自身)
    public boolean existsByAliAppId(String aliAppId, Long excludeId) {
        return lambdaQuery()
                .eq(AlipayIsvApp::getAliAppId, aliAppId)
                .ne(excludeId != null, AlipayIsvApp::getId, excludeId)
                .exists();
    }
}

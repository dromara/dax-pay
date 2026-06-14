package org.dromara.daxpay.channel.alipay.dao.isv;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvAppAuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝服务商应用授权认证配置
///
/// 服务商应用授权认证配置数据访问管理器，提供按应用ID查询和删除授权认证配置的方法。
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayIsvAppAuthConfigManager extends BaseManager<AlipayIsvAppAuthConfigMapper, AlipayIsvAppAuthConfig> {

    /// 根据应用ID查询授权认证配置
    public Optional<AlipayIsvAppAuthConfig> findByAppId(Long appId) {
        return lambdaQuery()
                .eq(AlipayIsvAppAuthConfig::getAppId, appId)
                .oneOpt();
    }

    /// 根据应用ID删除授权认证配置
    public void deleteByAppId(Long appId) {
        lambdaUpdate()
                .eq(AlipayIsvAppAuthConfig::getAppId, appId)
                .remove();
    }
}

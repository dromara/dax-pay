package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayMchAppAuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝直连商户应用授权认证配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayMchAppAuthConfigManager extends BaseManager<AlipayMchAppAuthConfigMapper, AlipayMchAppAuthConfig> {

    /// 根据应用ID查询授权认证配置
    public Optional<AlipayMchAppAuthConfig> findByAppId(Long appId) {
        return lambdaQuery()
                .eq(AlipayMchAppAuthConfig::getAppId, appId)
                .oneOpt();
    }

    /// 根据应用ID删除授权认证配置
    public void deleteByAppId(Long appId) {
        lambdaUpdate()
                .eq(AlipayMchAppAuthConfig::getAppId, appId)
                .remove();
    }
}

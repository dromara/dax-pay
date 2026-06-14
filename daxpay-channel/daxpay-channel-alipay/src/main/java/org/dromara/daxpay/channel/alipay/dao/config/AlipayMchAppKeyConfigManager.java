package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.config.AlipayMchAppKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 支付宝直连商户应用密钥配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class AlipayMchAppKeyConfigManager extends BaseManager<AlipayMchAppKeyConfigMapper, AlipayMchAppKeyConfig> {

    /// 根据应用ID查询密钥配置
    public Optional<AlipayMchAppKeyConfig> findByAppId(Long appId) {
        return lambdaQuery()
                .eq(AlipayMchAppKeyConfig::getAppId, appId)
                .oneOpt();
    }

    /// 根据应用ID删除密钥配置
    public void deleteByAppId(Long appId) {
        lambdaUpdate()
                .eq(AlipayMchAppKeyConfig::getAppId, appId)
                .remove();
    }
}

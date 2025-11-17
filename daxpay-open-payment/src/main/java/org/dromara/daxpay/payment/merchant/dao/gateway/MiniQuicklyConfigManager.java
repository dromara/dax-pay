package org.dromara.daxpay.payment.merchant.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.gateway.MiniQuicklyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 小程序快捷支付配置
 * @author xxm
 * @since 2025/10/10
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MiniQuicklyConfigManager extends BaseManager<MiniQuicklyConfigMapper, MiniQuicklyConfig> {

    /**
     * 根据appId查询
     */
    public Optional<MiniQuicklyConfig> findByAppId(String appId) {
        return findByField(MiniQuicklyConfig::getAppId, appId);
    }
}

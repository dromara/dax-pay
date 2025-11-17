package org.dromara.daxpay.payment.merchant.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateQrPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AggregateQrPayConfigManager extends BaseManager<AggregateQrPayConfigMapper, AggregateQrPayConfig> {

    /**
     * 查询配置
     */
    public Optional<AggregateQrPayConfig> findByAppId(String appId) {
        return findByField(AggregateQrPayConfig::getAppId, appId);
    }
}

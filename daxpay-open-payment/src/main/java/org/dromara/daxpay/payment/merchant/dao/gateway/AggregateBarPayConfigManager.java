package org.dromara.daxpay.payment.merchant.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateBarPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 网关聚合付款码支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AggregateBarPayConfigManager extends BaseManager<AggregateBarPayConfigMapper, AggregateBarPayConfig> {

    /**
     * 查询配置
     */
    public Optional<AggregateBarPayConfig> findByAppId(String appId) {
        return findByField(AggregateBarPayConfig::getAppId, appId);
    }
}

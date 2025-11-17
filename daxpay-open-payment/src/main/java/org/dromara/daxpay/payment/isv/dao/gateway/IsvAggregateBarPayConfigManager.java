package org.dromara.daxpay.payment.isv.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.isv.entity.gateway.IsvAggregateBarPayConfig;
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
public class IsvAggregateBarPayConfigManager extends BaseManager<IsvAggregateBarPayConfigMapper, IsvAggregateBarPayConfig> {

    /**
     * 查询配置
     */
    public Optional<IsvAggregateBarPayConfig> findByIsvNo(String isvNo) {
        return findByField(IsvAggregateBarPayConfig::getIsvNo, isvNo);
    }
}

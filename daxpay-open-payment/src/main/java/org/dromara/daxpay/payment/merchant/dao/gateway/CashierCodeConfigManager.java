package org.dromara.daxpay.payment.merchant.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import cn.bootx.platform.core.annotation.IgnoreTenant;
import org.dromara.daxpay.payment.merchant.entity.gateway.CashierCodeConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CashierCodeConfigManager extends BaseManager<CashierCodeConfigMapper, CashierCodeConfig> {

    public Optional<CashierCodeConfig> findByAppId(String appId) {
        return findByField(CashierCodeConfig::getAppId, appId);
    }


    @IgnoreTenant
    public Optional<CashierCodeConfig> findByAppIdNotTenant(String appId) {
        return findByField(CashierCodeConfig::getAppId, appId);
    }
}

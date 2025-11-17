package org.dromara.daxpay.payment.merchant.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.merchant.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayReadConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 网关支付读取配置
 * @author xxm
 * @since 2025/10/14
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class GatewayPayReadConfigManager extends BaseManager<GatewayPayReadConfigMapper, GatewayPayReadConfig> {

    /**
     * 查询配置
     */
    public Optional<GatewayPayReadConfig> findByAppId(String appId) {
        return findByField(MchAppBaseEntity::getAppId, appId);
    }
}

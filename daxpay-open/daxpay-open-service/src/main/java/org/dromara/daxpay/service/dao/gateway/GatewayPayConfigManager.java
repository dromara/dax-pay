package org.dromara.daxpay.service.dao.gateway;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.entity.gateway.GatewayPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 网关支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class GatewayPayConfigManager extends BaseManager<GatewayPayConfigMapper, GatewayPayConfig> {

    /**
     * 查询配置
     */
    public Optional<GatewayPayConfig> findByAppId(String appId) {
        return findByField(MchAppBaseEntity::getAppId,appId);
    }

    /**
     * 判重
     */
    public boolean existsByAppId(String appId){
        return existedByField(MchAppBaseEntity::getAppId, appId);
    }
}

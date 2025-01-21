package org.dromara.daxpay.service.dao.config.checkout;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.common.entity.MchAppBaseEntity;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 收银台配置
 * @author xxm
 * @since 2024/11/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CheckoutConfigManager extends BaseManager<CheckoutConfigMapper, CheckoutConfig> {

    /**
     * 查询配置
     */
    public Optional<CheckoutConfig> findByAppId(String appId) {
        return findByField(MchAppBaseEntity::getAppId,appId);
    }

    /**
     * 判重
     */
    public boolean existsByAppId(String appId){
        return existedByField(MchAppBaseEntity::getAppId, appId);
    }
}

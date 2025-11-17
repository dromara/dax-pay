package org.dromara.daxpay.payment.common.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.payment.common.entity.config.PlatformIntegrationConfig;
import org.springframework.stereotype.Component;

/**
 * 平台集成配置
 * @author xxm
 * @since 2025/1/15
 */
@Component
public class PlatformIntegrationConfigManager extends BaseManager<PlatformIntegrationConfigMapper, PlatformIntegrationConfig> {

    /**
     * 获取集成配置
     */
    public PlatformIntegrationConfig findIntegrationConfig() {
        return this.findById(1L).orElse(null);
    }
}

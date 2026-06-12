package org.dromara.daxpay.platform.system.dao.config;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.system.entity.config.sms.PlatformSmsConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 平台短信配置
///
@Repository
public class PlatformSmsConfigManager extends BaseManager<PlatformSmsConfigMapper, PlatformSmsConfig> {

    /// 查询默认配置
    public Optional<PlatformSmsConfig> findDefault() {
        return this.findByField(PlatformSmsConfig::isEnable, true);
    }

    /// 清除默认配置
    public void clearEnable() {
        this.lambdaUpdate()
                .set(PlatformSmsConfig::isEnable, false)
                .setIncrBy(PlatformSmsConfig::getVersion, 1)
                .update();
    }
}

package org.dromara.daxpay.platform.system.dao.config;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.system.entity.config.platform.SystemPlatformConfig;
import org.dromara.daxpay.platform.system.enums.PlatformConfigTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/// # 系统平台配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class SystemPlatformConfigManager extends BaseManager<SystemPlatformConfigMapper, SystemPlatformConfig> {

    /// 根据配置类型查询配置
    public SystemPlatformConfig findByConfigType(PlatformConfigTypeEnum configType) {
        return lambdaQuery()
                .eq(SystemPlatformConfig::getConfigType, configType.getCode())
                .one();
    }

    /// 根据配置类型查询配置
    public SystemPlatformConfig findByConfigType(String configType) {
        return lambdaQuery()
                .eq(SystemPlatformConfig::getConfigType, configType)
                .one();
    }

    /// 判断配置是否存在
    public boolean existsByConfigType(PlatformConfigTypeEnum configType) {
        return lambdaQuery()
                .eq(SystemPlatformConfig::getConfigType, configType.getCode())
                .exists();
    }
}

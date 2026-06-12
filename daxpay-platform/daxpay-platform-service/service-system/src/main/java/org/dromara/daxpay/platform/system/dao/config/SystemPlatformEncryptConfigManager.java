package org.dromara.daxpay.platform.system.dao.config;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.platform.system.entity.config.platform.SystemPlatformEncryptConfig;
import org.dromara.daxpay.platform.system.enums.EncryptPlatformConfigTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/// # 系统平台加密配置
///
@Slf4j
@Repository
@RequiredArgsConstructor
public class SystemPlatformEncryptConfigManager extends BaseManager<SystemPlatformEncryptConfigMapper, SystemPlatformEncryptConfig> {

    /// 根据配置类型查询配置
    public SystemPlatformEncryptConfig findByConfigType(EncryptPlatformConfigTypeEnum configType) {
        return lambdaQuery()
                .eq(SystemPlatformEncryptConfig::getConfigType, configType.getCode())
                .one();
    }

    /// 判断配置是否存在
    public boolean existsByConfigType(EncryptPlatformConfigTypeEnum configType) {
        return lambdaQuery()
                .eq(SystemPlatformEncryptConfig::getConfigType, configType.getCode())
                .exists();
    }
}

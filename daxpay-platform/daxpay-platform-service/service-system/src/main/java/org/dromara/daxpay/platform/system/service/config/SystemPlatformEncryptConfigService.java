package org.dromara.daxpay.platform.system.service.config;

import org.dromara.daxpay.platform.common.i18n.util.I18nUtil;
import org.dromara.daxpay.platform.common.json.util.JacksonUtil;
import org.dromara.daxpay.platform.system.dao.config.SystemPlatformEncryptConfigManager;
import org.dromara.daxpay.platform.system.entity.config.platform.SystemPlatformEncryptConfig;
import org.dromara.daxpay.platform.system.enums.EncryptPlatformConfigTypeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/// # 系统平台加密配置服务
///
/// 提供统一的加密配置管理，数据使用 AES-256-GCM 加密存储
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemPlatformEncryptConfigService {

    private final SystemPlatformEncryptConfigManager configManager;

    /// 获取配置数据
    /// @param configType 配置类型
    /// @param clazz 配置数据类
    /// @return 配置数据对象，如果不存在则返回null
    public <T> T getConfig(EncryptPlatformConfigTypeEnum configType, Class<T> clazz) {
        SystemPlatformEncryptConfig config = configManager.findByConfigType(configType);
        if (config == null || StrUtil.isBlank(config.getConfigData())) {
            return null;
        }
        return JacksonUtil.toBean(config.getConfigData(), clazz);
    }

    /// 获取配置数据，如果不存在则创建默认配置
    /// @param configType 配置类型
    /// @param clazz 配置数据类
    /// @param defaultValue 默认值
    /// @return 配置数据对象
    public <T> T getOrCreateConfig(EncryptPlatformConfigTypeEnum configType, Class<T> clazz, T defaultValue) {
        T config = this.getConfig(configType, clazz);
        if (config != null) {
            return config;
        }
        this.saveConfig(configType, defaultValue);
        return defaultValue;
    }

    /// 保存配置数据
    /// @param configType 配置类型
    /// @param data 配置数据对象
    public <T> void saveConfig(EncryptPlatformConfigTypeEnum configType, T data) {
        SystemPlatformEncryptConfig config = configManager.findByConfigType(configType);
        String jsonData = JacksonUtil.toJson(data);
        
        if (config == null) {
            config = new SystemPlatformEncryptConfig();
            config.setConfigType(configType.getCode());
            config.setConfigName(I18nUtil.getEnumName(configType));
            config.setConfigData(jsonData);
            config.setEnabled(true);
            try {
                configManager.save(config);
            } catch (DuplicateKeyException e) {
                log.warn("加密配置[{}]并发插入冲突，改为更新", configType.getCode());
                config = configManager.findByConfigType(configType);
                config.setConfigData(jsonData);
                configManager.updateById(config);
            }
        } else {
            config.setConfigData(jsonData);
            configManager.updateById(config);
        }
    }

    /// 更新配置数据
    /// @param configType 配置类型
    /// @param data 配置数据对象
    public <T> void updateConfig(EncryptPlatformConfigTypeEnum configType, T data) {
        this.saveConfig(configType, data);
    }

    /// 判断配置是否存在
    /// @param configType 配置类型
    /// @return 是否存在
    public boolean existsConfig(EncryptPlatformConfigTypeEnum configType) {
        return configManager.existsByConfigType(configType);
    }

    /// 获取原始配置实体
    /// @param configType 配置类型
    /// @return 配置实体
    public SystemPlatformEncryptConfig getConfigEntity(EncryptPlatformConfigTypeEnum configType) {
        return configManager.findByConfigType(configType);
    }
}


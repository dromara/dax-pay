package cn.daxpay.open.platform.system.service.config;

import cn.daxpay.open.platform.common.i18n.util.I18nUtil;
import cn.daxpay.open.platform.common.json.util.JacksonUtil;
import cn.daxpay.open.platform.system.dao.config.SystemPlatformConfigManager;
import cn.daxpay.open.platform.system.entity.config.platform.SystemPlatformConfig;
import cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/// # 系统平台配置服务
///
/// 提供统一的配置管理，使用JSON格式存储配置数据
@Slf4j
@Service
@RequiredArgsConstructor
public class SystemPlatformConfigService {

    private final SystemPlatformConfigManager configManager;

    /// 获取配置数据
    /// @param configType 配置类型
    /// @param clazz 配置数据类
    /// @return 配置数据对象，如果不存在则返回null
    public <T> T getConfig(PlatformConfigTypeEnum configType, Class<T> clazz) {
        SystemPlatformConfig config = configManager.findByConfigType(configType);
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
    public <T> T getOrCreateConfig(PlatformConfigTypeEnum configType, Class<T> clazz, T defaultValue) {
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
    public <T> void saveConfig(PlatformConfigTypeEnum configType, T data) {
        SystemPlatformConfig config = configManager.findByConfigType(configType);
        String jsonData = JacksonUtil.toJson(data);
        
        if (config == null) {
            config = new SystemPlatformConfig();
            config.setConfigType(configType.getCode());
            config.setConfigName(I18nUtil.getEnumName(configType));
            config.setConfigData(jsonData);
            config.setEnabled(true);
            try {
                configManager.save(config);
            } catch (DuplicateKeyException e) {
                log.warn("配置[{}]并发插入冲突，改为更新", configType.getCode());
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
    public <T> void updateConfig(PlatformConfigTypeEnum configType, T data) {
        this.saveConfig(configType, data);
    }

    /// 判断配置是否存在
    /// @param configType 配置类型
    /// @return 是否存在
    public boolean existsConfig(PlatformConfigTypeEnum configType) {
        return configManager.existsByConfigType(configType);
    }

    /// 获取原始配置实体
    /// @param configType 配置类型
    /// @return 配置实体
    public SystemPlatformConfig getConfigEntity(PlatformConfigTypeEnum configType) {
        return configManager.findByConfigType(configType);
    }
}


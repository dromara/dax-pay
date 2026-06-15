package org.dromara.daxpay.platform.capability.wechat.config.service;

import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import org.dromara.daxpay.platform.capability.wechat.config.convert.WechatConfigConvert;
import org.dromara.daxpay.platform.capability.wechat.config.dao.WechatConfigManager;
import org.dromara.daxpay.platform.capability.wechat.config.entity.WechatConfig;
import org.dromara.daxpay.platform.capability.wechat.config.param.WechatConfigParam;
import org.dromara.daxpay.platform.capability.wechat.config.result.WechatConfigResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/// # 微信配置服务
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatConfigService {
    private final WechatConfigManager wechatConfigManager;

    /// 获取微信配置
    public WechatConfig getConfig() {
        return this.getOrCreateConfig();
    }

    /// 获取微信配置结果
    public WechatConfigResult findConfig() {
        return this.getOrCreateConfig().toResult();
    }

    /// 获取微信配置, 如果不存在则创建
    public WechatConfig getOrCreateConfig() {
        var config = wechatConfigManager.findById(1L);
        if (config.isEmpty()) {
            var newConfig = new WechatConfig();
            newConfig.setId(1L);
            wechatConfigManager.save(newConfig);
            return newConfig;
        }
        return config.get();
    }

    /// 更新微信配置
    public void updateConfig(WechatConfigParam param) {
        var config = wechatConfigManager.findById(1L)
                // 微信: 微信配置不存在
                .orElseThrow(() -> new ConfigNotExistException("error.channel.wechat.platformConfigNotExist"));
        WechatConfigConvert.CONVERT.copy(param, config);
        wechatConfigManager.updateById(config);
    }
}

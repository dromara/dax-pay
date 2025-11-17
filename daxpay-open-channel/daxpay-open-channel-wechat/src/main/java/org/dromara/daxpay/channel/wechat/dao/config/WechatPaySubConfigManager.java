package org.dromara.daxpay.channel.wechat.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPaySubConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 微信子商户配置Manager
 * @author xxm
 * @since 2024/12/27
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WechatPaySubConfigManager extends BaseManager<WechatPaySubConfigMapper, WechatPaySubConfig> {

    /**
     * 根据应用ID查询
     */
    public Optional<WechatPaySubConfig> findByAppId(String appId) {
        return this.findByField(WechatPaySubConfig::getAppId, appId);
    }
}

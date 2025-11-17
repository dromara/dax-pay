package org.dromara.daxpay.channel.wechat.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.wechat.entity.config.WechatPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 微信支付配置Manager
 * @author xxm
 * @since 2021/3/1
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class WechatPayConfigManager extends BaseManager<WechatPayConfigMapper, WechatPayConfig> {

    /**
     * 根据应用ID查询
     */
    public Optional<WechatPayConfig> findByAppId(String appId) {
        return findByField(WechatPayConfig::getAppId, appId);
    }

}

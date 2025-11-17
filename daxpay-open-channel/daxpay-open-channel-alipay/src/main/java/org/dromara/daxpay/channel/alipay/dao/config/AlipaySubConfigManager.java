package org.dromara.daxpay.channel.alipay.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.config.AlipaySubConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付宝子商户配置Manager
 * @author xxm
 * @since 2024/11/1
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AlipaySubConfigManager extends BaseManager<AlipaySubConfigMapper, AlipaySubConfig> {

    /**
     * 根据应用ID查询
     */
    public Optional<AlipaySubConfig> findByAppId(String appId) {
        return findByField(AlipaySubConfig::getAppId, appId);
    }
}

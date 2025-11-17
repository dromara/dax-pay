package org.dromara.daxpay.channel.alipay.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 支付宝配置Manager
 * @author xxm
 * @since 2024/11/1
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AlipayConfigManager extends BaseManager<AlipayConfigMapper, AliPayConfig> {

    /**
     * 根据商户号和应用ID查询
     */
    public Optional<AliPayConfig> findByAppId(String appId) {
        return findByField(AliPayConfig::getAppId, appId);
    }
}

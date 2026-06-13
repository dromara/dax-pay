package org.dromara.daxpay.channel.wechat.dao.config;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.wechat.entity.config.WechatIsvKeyConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 微信服务商密钥配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvKeyConfigManager extends BaseManager<WechatIsvKeyConfigMapper, WechatIsvKeyConfig> {

    /// 根据产品编码查询（平台为唯一服务商，密钥全局唯一）
    public Optional<WechatIsvKeyConfig> findByProduct(String product) {
        return lambdaQuery()
                .eq(WechatIsvKeyConfig::getProduct, product)
                .oneOpt();
    }
}

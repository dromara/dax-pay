package org.dromara.daxpay.channel.wechat.dao.isv;

import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.channel.wechat.entity.isv.WechatIsvAppAuthConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/// # 微信服务商应用授权认证配置
///
@Slf4j
@Service
@RequiredArgsConstructor
public class WechatIsvAppAuthConfigManager extends BaseManager<WechatIsvAppAuthConfigMapper, WechatIsvAppAuthConfig> {

    /// 根据应用ID查询授权认证配置
    public Optional<WechatIsvAppAuthConfig> findByWechatIsvAppId(Long wechatIsvAppId) {
        return lambdaQuery()
                .eq(WechatIsvAppAuthConfig::getWechatIsvAppId, wechatIsvAppId)
                .oneOpt();
    }

    /// 根据应用ID删除授权认证配置
    public void deleteByWechatIsvAppId(Long wechatIsvAppId) {
        lambdaUpdate()
                .eq(WechatIsvAppAuthConfig::getWechatIsvAppId, wechatIsvAppId)
                .remove();
    }
}

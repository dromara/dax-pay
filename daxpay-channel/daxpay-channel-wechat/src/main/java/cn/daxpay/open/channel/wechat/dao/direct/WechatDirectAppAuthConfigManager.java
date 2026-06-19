package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppAuthConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 微信直连商户应用授权认证配置
///
/// 直连商户应用授权认证配置数据访问管理器，提供按应用ID查询和删除授权认证配置的方法。
///
@Repository
public class WechatDirectAppAuthConfigManager extends BaseManager<WechatDirectAppAuthConfigMapper, WechatDirectAppAuthConfig> {

    /// 根据应用ID查询授权认证配置
    public Optional<WechatDirectAppAuthConfig> findByWechatDirectAppId(Long wechatDirectAppId) {
        return lambdaQuery()
                .eq(WechatDirectAppAuthConfig::getWechatDirectAppId, wechatDirectAppId)
                .oneOpt();
    }

    /// 根据应用ID删除授权认证配置
    public void deleteByWechatDirectAppId(Long wechatDirectAppId) {
        lambdaUpdate()
                .eq(WechatDirectAppAuthConfig::getWechatDirectAppId, wechatDirectAppId)
                .remove();
    }
}

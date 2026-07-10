package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvMchAppAuthConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 微信服务商通道商户应用授权认证配置 Manager
///
/// 服务商通道商户应用授权认证配置数据访问管理器,提供按应用ID查询和删除授权认证配置的方法。
///
@Repository
public class WechatIsvMchAppAuthConfigManager extends BaseManager<WechatIsvMchAppAuthConfigMapper, WechatIsvMchAppAuthConfig> {

    /// 根据应用ID查询授权认证配置
    public Optional<WechatIsvMchAppAuthConfig> findByWechatIsvMchAppId(Long wechatIsvMchAppId) {
        return lambdaQuery()
                .eq(WechatIsvMchAppAuthConfig::getWechatIsvMchAppId, wechatIsvMchAppId)
                .oneOpt();
    }

    /// 根据应用ID查询授权认证配置（运行态认证使用, 忽略租户隔离）
    @IgnoreTenant
    public Optional<WechatIsvMchAppAuthConfig> findByWechatIsvMchAppIdNotTenant(Long wechatIsvMchAppId) {
        return lambdaQuery()
                .eq(WechatIsvMchAppAuthConfig::getWechatIsvMchAppId, wechatIsvMchAppId)
                .oneOpt();
    }

    /// 根据应用ID删除授权认证配置
    public void deleteByWechatIsvMchAppId(Long wechatIsvMchAppId) {
        lambdaUpdate()
                .eq(WechatIsvMchAppAuthConfig::getWechatIsvMchAppId, wechatIsvMchAppId)
                .remove();
    }
}

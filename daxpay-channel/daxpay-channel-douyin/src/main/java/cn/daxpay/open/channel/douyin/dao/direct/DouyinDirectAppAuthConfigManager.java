package cn.daxpay.open.channel.douyin.dao.direct;

import cn.daxpay.open.channel.douyin.entity.direct.DouyinDirectAppAuthConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.platform.core.annotation.IgnoreTenant;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 抖音直连商户应用授权认证配置
///
/// 直连商户应用授权认证配置数据访问管理器，提供按应用ID查询和删除授权认证配置的方法。
///
@Repository
public class DouyinDirectAppAuthConfigManager extends BaseManager<DouyinDirectAppAuthConfigMapper, DouyinDirectAppAuthConfig> {

    /// 根据应用ID查询授权认证配置
    public Optional<DouyinDirectAppAuthConfig> findByDouyinDirectAppId(Long douyinDirectAppId) {
        return lambdaQuery()
                .eq(DouyinDirectAppAuthConfig::getDouyinDirectAppId, douyinDirectAppId)
                .oneOpt();
    }

    /// 根据应用ID查询授权认证配置（运行态认证使用, 忽略租户隔离）
    @IgnoreTenant
    public Optional<DouyinDirectAppAuthConfig> findByDouyinDirectAppIdNotTenant(Long douyinDirectAppId) {
        return findByDouyinDirectAppId(douyinDirectAppId);
    }

    /// 根据应用ID删除授权认证配置
    public void deleteByDouyinDirectAppId(Long douyinDirectAppId) {
        lambdaUpdate()
                .eq(DouyinDirectAppAuthConfig::getDouyinDirectAppId, douyinDirectAppId)
                .remove();
    }
}

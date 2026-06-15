package org.dromara.daxpay.channel.douyin.dao.direct;

import org.dromara.daxpay.channel.douyin.entity.direct.DouyinDirectAppAuthConfig;
import org.dromara.daxpay.platform.common.mybatisplus.impl.BaseManager;
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

    /// 根据应用ID删除授权认证配置
    public void deleteByDouyinDirectAppId(Long douyinDirectAppId) {
        lambdaUpdate()
                .eq(DouyinDirectAppAuthConfig::getDouyinDirectAppId, douyinDirectAppId)
                .remove();
    }
}

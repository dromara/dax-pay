package cn.daxpay.open.platform.iam.dao.social;

import java.util.List;
import java.util.Optional;

import cn.daxpay.open.platform.iam.entity.social.SocialConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

/// # 第三方平台登录配置 Manager
///
/// 封装配置表的数据访问, 业务键(source)查询集中在此层, Service 层不直接使用 lambdaQuery.
///
@Repository
public class SocialConfigManager extends BaseManager<SocialConfigMapper, SocialConfig> {

    /// 查询全部配置(供 findAll 内存合并)
    public List<SocialConfig> listAll() {
        return lambdaQuery().list();
    }

    /// 根据平台编码查询
    public Optional<SocialConfig> findBySource(String source) {
        return lambdaQuery()
            .eq(SocialConfig::getSource, source)
            .oneOpt();
    }

    /// 根据平台编码查询已配置且启用的配置(供 SocialAuthRequestFactory 使用)
    public Optional<SocialConfig> findEnabledBySource(String source) {
        return lambdaQuery()
            .eq(SocialConfig::getSource, source)
            .eq(SocialConfig::isConfigured, true)
            .eq(SocialConfig::getEnabled, true)
            .oneOpt();
    }

    /// 查询所有已配置且启用的平台(供登录页展示可用平台)
    public List<SocialConfig> findAllEnabled() {
        return lambdaQuery()
            .eq(SocialConfig::isConfigured, true)
            .eq(SocialConfig::getEnabled, true)
            .list();
    }
}

package cn.daxpay.open.payment.merchant.dao.config;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.payment.merchant.entity.config.MchAppNotifyConfig;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 商户应用事件通知配置管理
///
@Repository
public class MchAppNotifyConfigManager extends BaseManager<MchAppNotifyConfigMapper, MchAppNotifyConfig> {

    /// 根据应用ID查询
    public Optional<MchAppNotifyConfig> findByAppId(String appId) {
        return findByField(MchAppNotifyConfig::getAppId, appId);
    }
}

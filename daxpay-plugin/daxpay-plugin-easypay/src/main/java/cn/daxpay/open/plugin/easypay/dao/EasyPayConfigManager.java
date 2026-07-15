package cn.daxpay.open.plugin.easypay.dao;

import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import cn.daxpay.open.plugin.easypay.entity.EasyPayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 易支付场景配置 Manager
///
@Repository
@RequiredArgsConstructor
public class EasyPayConfigManager extends BaseManager<EasyPayConfigMapper, EasyPayConfig> {

    /// 按易支付商户号查询
    public Optional<EasyPayConfig> findByPid(Integer pid) {
        return findByField(EasyPayConfig::getPid, pid);
    }

    /// 按应用号查询
    public Optional<EasyPayConfig> findByAppId(String appId) {
        return findByField(EasyPayConfig::getAppId, appId);
    }
}

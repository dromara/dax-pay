package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 网关支付配置管理器(码牌/聚合共用)
@Repository
public class GatewayPayConfigManager extends BaseManager<GatewayPayConfigMapper, GatewayPayConfig> {

    /// 按应用号查询
    public Optional<GatewayPayConfig> findByAppId(String appId) {
        return lambdaQuery()
                .eq(GatewayPayConfig::getAppId, appId)
                .oneOpt();
    }
}

package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 网关聚合扫码配置管理器
@Repository
public class GatewayAggregateConfigManager extends BaseManager<GatewayAggregateConfigMapper, GatewayAggregateConfig> {

    /// 按应用号查询
    public Optional<GatewayAggregateConfig> findByAppId(String appId) {
        return lambdaQuery()
                .eq(GatewayAggregateConfig::getAppId, appId)
                .oneOpt();
    }
}

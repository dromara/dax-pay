package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeConfig;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/// # 码牌支付策略配置管理器
@Repository
public class GatewayCodeConfigManager extends BaseManager<GatewayCodeConfigMapper, GatewayCodeConfig> {

    /// 按应用号查询
    public Optional<GatewayCodeConfig> findByAppId(String appId) {
        return lambdaQuery()
                .eq(GatewayCodeConfig::getAppId, appId)
                .oneOpt();
    }
}

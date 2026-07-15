package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeClientEnv;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 码牌支付策略客户端环境配置 Manager
@Repository
public class GatewayCodeClientEnvManager extends BaseManager<GatewayCodeClientEnvMapper, GatewayCodeClientEnv> {

    /// 按主表 ID 查询全部子配置
    public List<GatewayCodeClientEnv> findByConfigId(Long configId) {
        return lambdaQuery()
                .eq(GatewayCodeClientEnv::getConfigId, configId)
                .list();
    }

    /// 按主表 ID 删除全部子配置
    public void deleteByConfigId(Long configId) {
        lambdaUpdate()
                .eq(GatewayCodeClientEnv::getConfigId, configId)
                .remove();
    }

    /// 按主表 ID + 客户端环境 + 支付形态查询单条
    public GatewayCodeClientEnv findByConfigIdAndClientEnvAndPayForm(
            Long configId, String clientEnv, String payForm) {
        return lambdaQuery()
                .eq(GatewayCodeClientEnv::getConfigId, configId)
                .eq(GatewayCodeClientEnv::getClientEnv, clientEnv)
                .eq(GatewayCodeClientEnv::getPayForm, payForm)
                .one();
    }
}

package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayClientEnv;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 网关支付客户端环境配置 Manager(码牌/聚合共用)
@Repository
public class GatewayPayClientEnvManager extends BaseManager<GatewayPayClientEnvMapper, GatewayPayClientEnv> {

    /// 按主表 ID 查询全部子配置
    public List<GatewayPayClientEnv> findByConfigId(Long configId) {
        return lambdaQuery()
                .eq(GatewayPayClientEnv::getConfigId, configId)
                .list();
    }

    /// 按主表 ID 删除全部子配置
    public void deleteByConfigId(Long configId) {
        lambdaUpdate()
                .eq(GatewayPayClientEnv::getConfigId, configId)
                .remove();
    }

    /// 按主表 ID + 客户端环境 + 支付形态查询单条
    public GatewayPayClientEnv findByConfigIdAndClientEnvAndPayForm(
            Long configId, String clientEnv, String payForm) {
        return lambdaQuery()
                .eq(GatewayPayClientEnv::getConfigId, configId)
                .eq(GatewayPayClientEnv::getClientEnv, clientEnv)
                .eq(GatewayPayClientEnv::getPayForm, payForm)
                .one();
    }
}

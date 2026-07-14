package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateClientEnv;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 网关聚合扫码客户端环境配置 Manager
///
/// 管理客户端环境子表的查询与批量替换(先删后插)。
@Repository
public class GatewayAggregateClientEnvManager extends BaseManager<GatewayAggregateClientEnvMapper, GatewayAggregateClientEnv> {

    /// 按聚合配置主表 ID 查询全部客户端环境配置
    public List<GatewayAggregateClientEnv> findByConfigId(Long configId) {
        return lambdaQuery()
                .eq(GatewayAggregateClientEnv::getConfigId, configId)
                .list();
    }

    /// 按聚合配置主表 ID 删除全部客户端环境配置
    public void deleteByConfigId(Long configId) {
        lambdaUpdate()
                .eq(GatewayAggregateClientEnv::getConfigId, configId)
                .remove();
    }

    /// 按聚合配置主表 ID + 客户端环境编码查询单条
    public GatewayAggregateClientEnv findByConfigIdAndClientEnv(Long configId, String clientEnv) {
        return lambdaQuery()
                .eq(GatewayAggregateClientEnv::getConfigId, configId)
                .eq(GatewayAggregateClientEnv::getClientEnv, clientEnv)
                .one();
    }
}

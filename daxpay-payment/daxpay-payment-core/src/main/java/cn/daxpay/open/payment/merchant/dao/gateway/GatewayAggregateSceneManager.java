package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateScene;
import cn.daxpay.open.platform.common.mybatisplus.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/// # 网关聚合扫码场景配置 Manager
///
/// 管理场景子表的查询与批量替换(先删后插)。
@Repository
public class GatewayAggregateSceneManager extends BaseManager<GatewayAggregateSceneMapper, GatewayAggregateScene> {

    /// 按聚合配置主表 ID 查询全部场景配置
    public List<GatewayAggregateScene> findByConfigId(Long configId) {
        return lambdaQuery()
                .eq(GatewayAggregateScene::getConfigId, configId)
                .list();
    }

    /// 按聚合配置主表 ID 删除全部场景配置
    public void deleteByConfigId(Long configId) {
        lambdaUpdate()
                .eq(GatewayAggregateScene::getConfigId, configId)
                .remove();
    }

    /// 按聚合配置主表 ID + 场景编码查询单条
    public GatewayAggregateScene findByConfigIdAndScene(Long configId, String scene) {
        return lambdaQuery()
                .eq(GatewayAggregateScene::getConfigId, configId)
                .eq(GatewayAggregateScene::getScene, scene)
                .one();
    }
}

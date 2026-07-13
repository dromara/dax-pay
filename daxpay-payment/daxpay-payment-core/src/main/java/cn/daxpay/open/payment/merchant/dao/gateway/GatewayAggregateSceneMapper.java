package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateScene;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 网关聚合扫码场景配置 Mapper
@Mapper
public interface GatewayAggregateSceneMapper extends MPJBaseMapper<GatewayAggregateScene> {
}

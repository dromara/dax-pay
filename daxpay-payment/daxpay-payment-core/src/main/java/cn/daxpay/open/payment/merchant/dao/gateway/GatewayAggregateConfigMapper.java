package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 网关聚合扫码配置 Mapper
@Mapper
public interface GatewayAggregateConfigMapper extends MPJBaseMapper<GatewayAggregateConfig> {
}

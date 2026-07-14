package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayAggregateClientEnv;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 网关聚合扫码客户端环境配置 Mapper
@Mapper
public interface GatewayAggregateClientEnvMapper extends MPJBaseMapper<GatewayAggregateClientEnv> {
}

package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayCodeClientEnv;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 码牌支付策略客户端环境配置 Mapper
@Mapper
public interface GatewayCodeClientEnvMapper extends MPJBaseMapper<GatewayCodeClientEnv> {
}

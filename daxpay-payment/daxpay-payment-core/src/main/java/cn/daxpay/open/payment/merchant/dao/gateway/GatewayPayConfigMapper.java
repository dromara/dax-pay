package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 网关支付配置 Mapper
@Mapper
public interface GatewayPayConfigMapper extends MPJBaseMapper<GatewayPayConfig> {
}

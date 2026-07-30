package cn.daxpay.open.payment.merchant.dao.gateway;

import cn.daxpay.open.payment.merchant.entity.gateway.GatewayPayClientEnv;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 网关支付客户端环境配置 Mapper
@Mapper
public interface GatewayPayClientEnvMapper extends MPJBaseMapper<GatewayPayClientEnv> {
}

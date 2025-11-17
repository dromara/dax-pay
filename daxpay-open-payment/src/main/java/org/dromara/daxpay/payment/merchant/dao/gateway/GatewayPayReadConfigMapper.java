package org.dromara.daxpay.payment.merchant.dao.gateway;

import org.dromara.daxpay.payment.merchant.entity.gateway.GatewayPayReadConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关支付读取配置
 * @author xxm
 * @since 2025/10/14
 */
@Mapper
public interface GatewayPayReadConfigMapper extends MPJBaseMapper<GatewayPayReadConfig> {
}

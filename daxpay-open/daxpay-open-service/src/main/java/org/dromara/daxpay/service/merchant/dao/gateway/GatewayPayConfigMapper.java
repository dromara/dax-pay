package org.dromara.daxpay.service.merchant.dao.gateway;

import org.dromara.daxpay.service.merchant.entity.gateway.GatewayPayConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *网关支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface GatewayPayConfigMapper extends MPJBaseMapper<GatewayPayConfig> {
}

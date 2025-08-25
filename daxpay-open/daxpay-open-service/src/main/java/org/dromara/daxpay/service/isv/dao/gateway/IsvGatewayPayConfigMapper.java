package org.dromara.daxpay.service.isv.dao.gateway;

import org.dromara.daxpay.service.isv.entity.gateway.IsvGatewayPayConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 *网关支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvGatewayPayConfigMapper extends MPJBaseMapper<IsvGatewayPayConfig> {
}

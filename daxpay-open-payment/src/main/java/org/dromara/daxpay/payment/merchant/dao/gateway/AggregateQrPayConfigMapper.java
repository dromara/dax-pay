package org.dromara.daxpay.payment.merchant.dao.gateway;

import org.dromara.daxpay.payment.merchant.entity.gateway.AggregateQrPayConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface AggregateQrPayConfigMapper extends MPJBaseMapper<AggregateQrPayConfig> {
}

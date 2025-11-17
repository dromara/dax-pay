package org.dromara.daxpay.payment.isv.dao.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvAggregatePayConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关聚合支付配置
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvAggregatePayConfigMapper extends MPJBaseMapper<IsvAggregatePayConfig> {
}

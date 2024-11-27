package org.dromara.daxpay.service.dao.config.checkout;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutAggregateConfig;

/**
 * 收银台聚合支付配置
 * @author xxm
 * @since 2024/11/27
 */
@Mapper
public interface CheckoutAggregateConfigMapper extends MPJBaseMapper<CheckoutAggregateConfig> {
}

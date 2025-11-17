package org.dromara.daxpay.payment.merchant.dao.gateway;

import org.dromara.daxpay.payment.merchant.entity.gateway.CheckoutCounterConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface CheckoutCounterMapper extends MPJBaseMapper<CheckoutCounterConfig> {
}

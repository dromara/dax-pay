package org.dromara.daxpay.payment.isv.dao.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvCheckoutCounterConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvCheckoutCounterConfigMapper extends MPJBaseMapper<IsvCheckoutCounterConfig> {
}

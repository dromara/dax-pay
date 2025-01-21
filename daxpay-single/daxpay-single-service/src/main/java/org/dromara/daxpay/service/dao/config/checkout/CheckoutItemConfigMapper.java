package org.dromara.daxpay.service.dao.config.checkout;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutItemConfig;

/**
 * 收银台配置项
 * @author xxm
 * @since 2024/11/26
 */
@Mapper
public interface CheckoutItemConfigMapper extends MPJBaseMapper<CheckoutItemConfig> {
}

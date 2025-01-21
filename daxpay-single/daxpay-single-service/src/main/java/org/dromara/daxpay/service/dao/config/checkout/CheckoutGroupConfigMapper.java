package org.dromara.daxpay.service.dao.config.checkout;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutGroupConfig;

/**
 * 收银台类目配置
 * @author xxm
 * @since 2024/11/25
 */
@Mapper
public interface CheckoutGroupConfigMapper extends MPJBaseMapper<CheckoutGroupConfig> {
}

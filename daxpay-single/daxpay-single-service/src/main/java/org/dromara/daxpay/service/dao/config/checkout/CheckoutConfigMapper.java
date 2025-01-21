package org.dromara.daxpay.service.dao.config.checkout;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutConfig;

/**   
 * 收银台配置
 * @author xxm  
 * @since 2024/11/27 
 */
@Mapper
public interface CheckoutConfigMapper extends MPJBaseMapper<CheckoutConfig> {
}

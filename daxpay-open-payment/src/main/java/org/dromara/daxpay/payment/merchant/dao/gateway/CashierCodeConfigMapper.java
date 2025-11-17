package org.dromara.daxpay.payment.merchant.dao.gateway;

import org.dromara.daxpay.payment.merchant.entity.gateway.CashierCodeConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收银码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Mapper
public interface CashierCodeConfigMapper extends MPJBaseMapper<CashierCodeConfig> {
}

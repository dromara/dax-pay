package org.dromara.daxpay.service.dao.config.cashier;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.daxpay.service.entity.config.cashier.CashierCodeConfig;

/**
 * 收银台码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Mapper
public interface CashierCodeConfigMapper extends MPJBaseMapper<CashierCodeConfig> {
}

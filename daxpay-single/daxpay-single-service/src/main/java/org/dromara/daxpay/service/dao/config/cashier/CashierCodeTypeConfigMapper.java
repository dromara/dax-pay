package org.dromara.daxpay.service.dao.config.cashier;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.dromara.daxpay.service.entity.config.cashier.CashierCodeTypeConfig;

/**
 * 渠道码类型配置表 Mapper 接口
 * @author xxm
 * @since 2024/11/20
 */
@Mapper
public interface CashierCodeTypeConfigMapper extends MPJBaseMapper<CashierCodeTypeConfig> {
}

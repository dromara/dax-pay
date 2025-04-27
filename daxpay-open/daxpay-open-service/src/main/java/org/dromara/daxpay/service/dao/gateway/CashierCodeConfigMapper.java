package org.dromara.daxpay.service.dao.gateway;

import org.dromara.daxpay.service.entity.gateway.CashierCodeConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收银台码牌配置
 * @author xxm
 * @since 2024/11/20
 */
@Mapper
public interface CashierCodeConfigMapper extends MPJBaseMapper<CashierCodeConfig> {
}

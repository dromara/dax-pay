package org.dromara.daxpay.service.isv.dao.gateway;

import org.dromara.daxpay.service.isv.entity.gateway.IsvCashierItemConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关收银台配置项
 * @author xxm
 * @since 2025/3/19
 */
@Mapper
public interface IsvCashierItemConfigMapper extends MPJBaseMapper<IsvCashierItemConfig> {
}

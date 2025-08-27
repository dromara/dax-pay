package org.dromara.daxpay.service.isv.dao.gateway;

import org.dromara.daxpay.service.isv.entity.gateway.IsvAggregateBarPayConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网关聚合付款码支付配置
 * @author xxm
 * @since 2025/3/24
 */
@Mapper
public interface IsvAggregateBarPayConfigMapper extends MPJBaseMapper<IsvAggregateBarPayConfig> {
}

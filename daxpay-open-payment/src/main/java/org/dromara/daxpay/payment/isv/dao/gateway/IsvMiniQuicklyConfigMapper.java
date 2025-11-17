package org.dromara.daxpay.payment.isv.dao.gateway;

import org.dromara.daxpay.payment.isv.entity.gateway.IsvMiniQuicklyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 小程序快捷支付配置
 * @author xxm
 * @since 2025/10/10
 */
@Mapper
public interface IsvMiniQuicklyConfigMapper extends MPJBaseMapper<IsvMiniQuicklyConfig> {
}

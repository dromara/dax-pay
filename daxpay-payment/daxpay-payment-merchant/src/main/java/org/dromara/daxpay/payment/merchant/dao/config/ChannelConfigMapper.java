package org.dromara.daxpay.payment.merchant.dao.config;

import org.dromara.daxpay.payment.masterdata.config.entity.ChannelConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 通道支付配置
///
@Mapper
public interface ChannelConfigMapper extends MPJBaseMapper<ChannelConfig> {
}

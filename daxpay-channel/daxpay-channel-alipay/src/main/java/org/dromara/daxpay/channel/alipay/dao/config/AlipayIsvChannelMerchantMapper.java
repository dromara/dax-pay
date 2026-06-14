package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvChannelMerchant;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝服务商通道商户绑定
///
@Mapper
public interface AlipayIsvChannelMerchantMapper extends MPJBaseMapper<AlipayIsvChannelMerchant> {
}

package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayDirectChannelMerchant;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝直连通道商户绑定
///
@Mapper
public interface AlipayDirectChannelMerchantMapper extends MPJBaseMapper<AlipayDirectChannelMerchant> {
}

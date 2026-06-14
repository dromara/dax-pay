package org.dromara.daxpay.channel.alipay.dao.direct;

import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectChannelMerchant;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝直连通道商户绑定
///
/// 支付宝直连通道商户 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayDirectChannelMerchantMapper extends MPJBaseMapper<AlipayDirectChannelMerchant> {
}

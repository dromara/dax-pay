package cn.daxpay.open.channel.stripe.dao;

import cn.daxpay.open.channel.stripe.entity.StripeChannelMerchant;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # Stripe 通道商户绑定
///
/// Stripe 通道商户 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface StripeChannelMerchantMapper extends MPJBaseMapper<StripeChannelMerchant> {
}

package cn.daxpay.open.channel.stripe.dao;

import cn.daxpay.open.channel.stripe.entity.StripeKeyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # Stripe 密钥配置
///
@Mapper
public interface StripeKeyConfigMapper extends MPJBaseMapper<StripeKeyConfig> {
}

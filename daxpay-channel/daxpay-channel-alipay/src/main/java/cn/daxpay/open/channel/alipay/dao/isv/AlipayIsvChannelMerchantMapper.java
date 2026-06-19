package cn.daxpay.open.channel.alipay.dao.isv;

import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvChannelMerchant;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝服务商通道商户绑定
///
/// 支付宝服务商通道商户 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayIsvChannelMerchantMapper extends MPJBaseMapper<AlipayIsvChannelMerchant> {
}

package org.dromara.daxpay.channel.wechat.dao.direct;

import org.dromara.daxpay.channel.wechat.entity.direct.WechatDirectChannelMerchant;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信直连通道商户绑定
///
/// 微信直连通道商户 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface WechatDirectChannelMerchantMapper extends MPJBaseMapper<WechatDirectChannelMerchant> {
}

package cn.daxpay.open.channel.wechat.dao.direct;

import cn.daxpay.open.channel.wechat.entity.direct.WechatDirectAppCapability;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信直连商户应用支付能力关联 Mapper
///
/// 继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface WechatDirectAppCapabilityMapper extends MPJBaseMapper<WechatDirectAppCapability> {
}

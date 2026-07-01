package cn.daxpay.open.channel.wechat.dao.isv;

import cn.daxpay.open.channel.wechat.entity.isv.WechatIsvAppCapability;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 微信服务商应用支付能力关联 Mapper
///
/// 继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface WechatIsvAppCapabilityMapper extends MPJBaseMapper<WechatIsvAppCapability> {
}

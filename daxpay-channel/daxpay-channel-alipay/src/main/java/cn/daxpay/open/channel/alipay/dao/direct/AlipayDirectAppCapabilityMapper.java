package cn.daxpay.open.channel.alipay.dao.direct;

import cn.daxpay.open.channel.alipay.entity.direct.AlipayDirectAppCapability;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝直连商户应用支付能力关联
///
/// 支付宝直连商户应用支付能力关联 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayDirectAppCapabilityMapper extends MPJBaseMapper<AlipayDirectAppCapability> {
}

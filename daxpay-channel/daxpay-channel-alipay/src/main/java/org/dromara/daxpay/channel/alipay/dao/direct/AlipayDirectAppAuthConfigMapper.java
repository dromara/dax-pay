package org.dromara.daxpay.channel.alipay.dao.direct;

import org.dromara.daxpay.channel.alipay.entity.direct.AlipayDirectAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝直连商户应用授权认证配置
///
/// 支付宝直连商户应用授权认证配置 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayDirectAppAuthConfigMapper extends MPJBaseMapper<AlipayDirectAppAuthConfig> {
}

package cn.daxpay.open.channel.alipay.dao.isv;

import cn.daxpay.open.channel.alipay.entity.isv.AlipayIsvAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝服务商应用授权认证配置
///
/// 支付宝服务商应用授权认证配置 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayIsvAppAuthConfigMapper extends MPJBaseMapper<AlipayIsvAppAuthConfig> {
}

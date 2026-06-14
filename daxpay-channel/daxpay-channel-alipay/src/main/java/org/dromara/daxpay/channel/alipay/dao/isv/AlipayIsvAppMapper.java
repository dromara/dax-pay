package org.dromara.daxpay.channel.alipay.dao.isv;

import org.dromara.daxpay.channel.alipay.entity.isv.AlipayIsvApp;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝服务商应用
///
/// 支付宝服务商应用 MyBatis-Plus Mapper，继承 MPJBaseMapper 支持多表联查。
///
@Mapper
public interface AlipayIsvAppMapper extends MPJBaseMapper<AlipayIsvApp> {
}

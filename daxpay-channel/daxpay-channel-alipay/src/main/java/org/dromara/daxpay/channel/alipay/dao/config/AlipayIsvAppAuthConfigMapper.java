package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝服务商应用授权认证配置
///
@Mapper
public interface AlipayIsvAppAuthConfigMapper extends MPJBaseMapper<AlipayIsvAppAuthConfig> {
}

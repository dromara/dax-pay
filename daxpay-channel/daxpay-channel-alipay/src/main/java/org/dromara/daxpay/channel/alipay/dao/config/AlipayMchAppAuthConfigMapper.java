package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayMchAppAuthConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝直连商户应用授权认证配置
///
@Mapper
public interface AlipayMchAppAuthConfigMapper extends MPJBaseMapper<AlipayMchAppAuthConfig> {
}

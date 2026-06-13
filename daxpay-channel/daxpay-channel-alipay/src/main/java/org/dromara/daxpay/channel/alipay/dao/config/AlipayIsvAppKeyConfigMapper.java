package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayIsvAppKeyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝服务商应用密钥配置
///
@Mapper
public interface AlipayIsvAppKeyConfigMapper extends MPJBaseMapper<AlipayIsvAppKeyConfig> {
}

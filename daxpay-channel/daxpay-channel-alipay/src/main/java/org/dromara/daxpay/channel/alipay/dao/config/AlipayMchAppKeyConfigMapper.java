package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AlipayMchAppKeyConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 支付宝直连商户应用密钥配置
///
@Mapper
public interface AlipayMchAppKeyConfigMapper extends MPJBaseMapper<AlipayMchAppKeyConfig> {
}

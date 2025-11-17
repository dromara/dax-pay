package org.dromara.daxpay.channel.alipay.dao.config;

import org.dromara.daxpay.channel.alipay.entity.config.AliPayConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 支付宝配置Mapper
 * @author xxm
 * @since 2024/11/1
 */
@Mapper
public interface AlipayConfigMapper extends MPJBaseMapper<AliPayConfig> {
}

package org.dromara.daxpay.service.dao.config;

import org.dromara.daxpay.service.entity.config.ChannelConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 通道支付配置
 * @author xxm
 * @since 2024/6/25
 */
@Mapper
public interface ChannelConfigMapper extends MPJBaseMapper<ChannelConfig> {
}

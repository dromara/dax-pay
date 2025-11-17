package org.dromara.daxpay.payment.common.dao.config;

import org.dromara.daxpay.payment.common.entity.config.PlatformIntegrationConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 平台集成配置
 * @author xxm
 * @since 2025/1/15
 */
@Mapper
public interface PlatformIntegrationConfigMapper extends MPJBaseMapper<PlatformIntegrationConfig> {
}

package org.dromara.daxpay.platform.system.dao.config;

import org.dromara.daxpay.platform.system.entity.config.sms.PlatformSmsConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/// # 平台短信配置
///
@Mapper
public interface PlatformSmsConfigMapper extends MPJBaseMapper<PlatformSmsConfig> {
}

package org.dromara.daxpay.service.common.dao.config;

import org.dromara.daxpay.service.common.entity.config.PlatformUrlConfig;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统地址配置
 * @author xxm
 * @since 2025/6/29
 */
@Mapper
public interface PlatformUrlConfigMapper extends MPJBaseMapper<PlatformUrlConfig> {
}

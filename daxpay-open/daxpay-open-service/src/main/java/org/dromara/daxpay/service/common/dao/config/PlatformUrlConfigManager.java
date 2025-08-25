package org.dromara.daxpay.service.common.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.common.entity.config.PlatformUrlConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2025/6/29
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PlatformUrlConfigManager extends BaseManager<PlatformUrlConfigMapper, PlatformUrlConfig> {
}

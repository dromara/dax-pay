package org.dromara.daxpay.service.common.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import org.dromara.daxpay.service.common.entity.config.PlatformBasicConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/7/17
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PlatformBasicConfigManager extends BaseManager<PlatformBasicConfigMapper, PlatformBasicConfig> {
}

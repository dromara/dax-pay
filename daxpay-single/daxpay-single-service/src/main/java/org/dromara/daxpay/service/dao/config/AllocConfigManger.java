package org.dromara.daxpay.service.dao.config;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.config.AllocConfig;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/10/6
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class AllocConfigManger extends BaseManager<AllocConfigMapper, AllocConfig> {
}

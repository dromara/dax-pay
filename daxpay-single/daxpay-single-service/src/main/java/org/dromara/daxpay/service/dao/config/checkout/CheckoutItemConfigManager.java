package org.dromara.daxpay.service.dao.config.checkout;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutItemConfig;
import org.springframework.stereotype.Repository;

/**
 * 收银台配置项
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CheckoutItemConfigManager extends BaseManager<CheckoutItemConfigMapper, CheckoutItemConfig> {
}

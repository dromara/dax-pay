package org.dromara.daxpay.service.dao.config.checkout;

import cn.bootx.platform.common.mybatisplus.impl.BaseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.entity.config.checkout.CheckoutConfig;
import org.springframework.stereotype.Repository;

/**
 *
 * @author xxm
 * @since 2024/11/25
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class CheckoutConfigManager extends BaseManager<CheckoutConfigMapper, CheckoutConfig> {
}

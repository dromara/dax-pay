package org.dromara.daxpay.service.service.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutGroupConfigManager;
import org.dromara.daxpay.service.dao.config.checkout.CheckoutItemConfigManager;
import org.springframework.stereotype.Service;

/**
 * 收银台配置
 * @author xxm
 * @since 2024/11/26
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutConfigService {
    private final CheckoutConfigManager checkoutConfigManager;
    private final CheckoutGroupConfigManager checkoutGroupConfigManager;
    private final CheckoutItemConfigManager checkoutItemConfigManager;




}

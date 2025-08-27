package org.dromara.daxpay.service.merchant.cache;

import org.dromara.daxpay.core.exception.ConfigNotEnableException;
import org.dromara.daxpay.service.merchant.dao.info.MerchantManager;
import org.dromara.daxpay.service.merchant.entity.info.Merchant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 商户缓存服务
 * @author xxm
 * @since 2024/6/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCacheService {
    private final MerchantManager merchantManager;

    /**
     * 获取商户配置
     */
    @Cacheable(value = "cache:merchant", key = "#mchNo")
    public Merchant get(String mchNo) {
        return merchantManager.findByMchNo(mchNo).orElseThrow(() -> new ConfigNotEnableException("未找到指定的商户配置"));
    }
}

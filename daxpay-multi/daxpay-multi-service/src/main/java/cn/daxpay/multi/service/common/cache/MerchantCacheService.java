package cn.daxpay.multi.service.common.cache;

import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.service.dao.merchant.MerchantManager;
import cn.daxpay.multi.service.entity.merchant.Merchant;
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

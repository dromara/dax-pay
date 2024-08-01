package cn.daxpay.multi.service.common.cache;

import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.service.dao.merchant.MerchantManager;
import cn.daxpay.multi.service.entity.merchant.Merchant;
import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 商户缓存服务
 * @author xxm
 * @since 2024/6/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantCacheService {
    private final Cache<String, Merchant> cache = CacheUtil.newLRUCache(200, 15 * 60 * 1000);

    private final MerchantManager merchantManager;

    /**
     * 获取通道配置
     */
    @Cacheable(value = "cache:merchant", key = "#mchNo")
    public Merchant get(String mchNo) {
        var merchant = cache.get(mchNo);
        if (Objects.isNull(merchant)) {
            merchant = merchantManager.findByMchNo(mchNo)
                    .orElseThrow(() -> new ConfigNotEnableException("未找到指定的商户配置"));
//            cache.put(mchNo, merchant);
        }
        return merchant;
    }

    /**
     * 清楚并更新通道配置
     */
    public void remove(String mchNo) {
        cache.remove(mchNo);
    }

}

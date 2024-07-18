package cn.daxpay.multi.service.common.cache;

import cn.daxpay.multi.core.exception.ConfigNotEnableException;
import cn.daxpay.multi.service.dao.merchant.MchAppManager;
import cn.daxpay.multi.service.entity.merchant.MchApp;
import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 商户应用缓存服务
 * @author xxm
 * @since 2024/6/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MchAppCacheService {
    private final Cache<String, MchApp> cache = CacheUtil.newLRUCache(200,15 * 60 * 1000);

    private final MchAppManager mchAppManager;

    /**
     * 获取通道配置
     */
    public MchApp get(String appId) {
        var mchApp = cache.get(appId);
        if (Objects.isNull(mchApp)) {
            mchApp = mchAppManager.findByAppId(appId)
                    .orElseThrow(() -> new ConfigNotEnableException("未找到指定的应用配置"));
            cache.put(appId, mchApp);
        }
        return mchApp;
    }

    /**
     * 清楚并更新通道配置
     */
    public void remove(String appId) {
        cache.remove(appId);
    }

}

package cn.daxpay.open.payment.route.service.support;

import cn.daxpay.open.payment.route.service.runtime.PayRouteBundleService;
import cn.daxpay.open.payment.trade.notice.util.NoticeTxUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/// # 通道路由数据包缓存失效入口
///
/// 写侧统一经此失效 `payment:route-bundle`(按 appId)，读侧见 [PayRouteBundleService#loadBundle]。
///
/// **为何不用 @CacheEvict**：`@Transactional` 与 `@CacheEvict` 标在同一方法时，事务拦截器与缓存拦截器的
/// order 均为默认 LOWEST_PRECEDENCE(项目未显式指定)，失效有落在事务提交之前的可能；窗口内并发支付请求
/// 回源读到旧配置并回填缓存，旧配置会一直生效到 L2 TTL(默认 30 分钟)。改为事务提交后失效即可消除该窗口
/// (提交后失效不存在反向问题: 提交前写入缓存的旧值会被随后的失效清掉)。
@Component
@RequiredArgsConstructor
@Slf4j
public class PayRouteBundleCacheEvictor {

    /// 缓存名(与 [PayRouteBundleService#loadBundle] 的 @Cacheable 保持一致)
    public static final String CACHE_NAME = "payment:route-bundle";

    private final CacheManager cacheManager;

    /// 事务提交后失效指定应用的路由数据包；无活跃事务则立即失效
    ///
    /// 回滚时不失效(提交前缓存本就未被污染，无需清理)。
    /// 失效异常只记日志不外抛：此时事务已提交、无法回滚，抛错只会让「保存成功」变成 500。
    public void evictAfterCommit(String appId) {
        if (StrUtil.isBlank(appId)) {
            return;
        }
        NoticeTxUtil.afterCommit(() -> {
            try {
                Cache cache = cacheManager.getCache(CACHE_NAME);
                if (cache == null) {
                    return;
                }
                cache.evict(appId);
                log.debug("通道路由缓存已失效: appId={}", appId);
            } catch (Exception e) {
                log.warn("通道路由缓存失效失败, 最迟将在 TTL 到期后自动恢复: appId={}", appId, e);
            }
        });
    }
}

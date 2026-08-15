package cn.daxpay.open.demo.cache.controller;

import cn.daxpay.open.demo.cache.listener.DemoCacheInvalidationListener;
import cn.daxpay.open.demo.cache.model.CacheDemoProduct;
import cn.daxpay.open.demo.cache.result.CacheDemoReadResult;
import cn.daxpay.open.demo.cache.result.CacheInvalidationEventResult;
import cn.daxpay.open.demo.cache.result.CacheL1StatusResult;
import cn.daxpay.open.demo.cache.service.CacheDemoService;
import cn.daxpay.open.platform.core.annotation.IgnoreAuth;
import cn.daxpay.open.platform.core.rest.Res;
import cn.daxpay.open.platform.core.rest.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

/// # 缓存读写演示接口
///
/// 演示二级缓存(L1 Caffeine + L2 Redis)的读写与失效, 重点验证 List&lt;POJO&gt; 泛型容器缓存
/// 经定型序列化后 L2 命中能还原真实类型(而非 LinkedHashMap)。
///
/// 控制层类型探针: @Cacheable 返回后取实际运行时类型返回给前端——
/// L1 命中是存活对象、L2 命中是定型反序列化对象, 均为真实实体类型;
/// 若未注册类型且经 L2 反序列化, 会得到 java.util.LinkedHashMap(泛型擦除下赋值不报错, 探针可检出)。
///
/// 鉴权：URL 前缀 `/demo/**` 已在白名单，类上叠加 `@IgnoreAuth` 双保险。
@Slf4j
@IgnoreAuth
@Tag(name = "缓存读写演示")
@RestController
@RequestMapping("/demo/cache")
@RequiredArgsConstructor
public class CacheDemoController {

    private final CacheDemoService cacheDemoService;

    private final DemoCacheInvalidationListener invalidationListener;

    private final CacheManager cacheManager;

    /// 读取单对象缓存(带类型探针)
    @Operation(summary = "按编码读取商品(单对象缓存演示)")
    @GetMapping("/product")
    public Result<CacheDemoReadResult> getProduct(
            @Parameter(description = "商品编码, 如 P001") @RequestParam("code") String code) {
        long start = System.currentTimeMillis();
        Object value = cacheDemoService.loadProduct(code);
        long cost = System.currentTimeMillis() - start;
        String elementType = value == null ? "-" : value.getClass().getName();
        return Res.ok(new CacheDemoReadResult()
                .setCacheName(CacheDemoService.PRODUCT_CACHE)
                .setCacheKey(code)
                .setData(value)
                .setElementType(elementType)
                .setExpectedType(CacheDemoProduct.class.getName())
                .setTypeMatched(CacheDemoProduct.class.getName().equals(elementType))
                .setMethodLoads(cacheDemoService.getProductLoads())
                .setCostMillis(cost));
    }

    /// 读取列表缓存(泛型容器, 演示重点)
    @Operation(summary = "按分类读取商品列表(List<T> 泛型容器缓存演示)")
    @GetMapping("/product-list")
    public Result<CacheDemoReadResult> getProductList(
            @Parameter(description = "分类编码, 如 drink / food") @RequestParam("category") String category) {
        long start = System.currentTimeMillis();
        List<CacheDemoProduct> list = cacheDemoService.loadProductList(category);
        long cost = System.currentTimeMillis() - start;
        // 元素类型探针: 列表本身总是 List, 关键看元素是真实实体还是 LinkedHashMap
        String elementType = list.isEmpty() ? "-" : list.get(0).getClass().getName();
        return Res.ok(new CacheDemoReadResult()
                .setCacheName(CacheDemoService.PRODUCT_LIST_CACHE)
                .setCacheKey(category)
                .setData(list)
                .setElementType(elementType)
                .setExpectedType(CacheDemoProduct.class.getName())
                .setTypeMatched(CacheDemoProduct.class.getName().equals(elementType))
                .setMethodLoads(cacheDemoService.getListLoads())
                .setCostMillis(cost));
    }

    /// 失效单对象缓存
    @Operation(summary = "失效单对象缓存")
    @DeleteMapping("/product")
    public Result<Void> evictProduct(
            @Parameter(description = "商品编码") @RequestParam("code") String code) {
        cacheDemoService.evictProduct(code);
        return Res.ok();
    }

    /// 失效列表缓存
    @Operation(summary = "失效列表缓存")
    @DeleteMapping("/product-list")
    public Result<Void> evictProductList(
            @Parameter(description = "分类编码") @RequestParam("category") String category) {
        cacheDemoService.evictProductList(category);
        return Res.ok();
    }

    /// 修改商品名称并触发缓存失效广播(演示: 修改内容 → L1 集群失效通知)
    ///
    /// 一次修改触发两条广播: 单对象缓存精确失效(EVICT) + 列表缓存全量失效(CLEAR),
    /// 前端可通过 `/invalidation-events` 观察本节点收到的通知, 通过 `/l1-status` 观察 L1 变化
    @Operation(summary = "修改商品名称并触发缓存失效广播")
    @PutMapping("/product")
    public Result<CacheDemoProduct> updateProduct(
            @Parameter(description = "商品编码") @RequestParam("code") String code,
            @Parameter(description = "新的商品名称") @RequestParam("name") String name) {
        CacheDemoProduct updated = cacheDemoService.updateProduct(code, name);
        // 编码不存在返回 null data, 由前端提示
        return Res.ok(updated);
    }

    /// 查询本节点最近收到的缓存失效广播事件
    ///
    /// 事件来自与平台 CacheInvalidationConsumer 并列的演示订阅者, 只记录不删缓存
    @Operation(summary = "查询缓存失效广播事件(本节点订阅观察)")
    @GetMapping("/invalidation-events")
    public Result<List<CacheInvalidationEventResult>> invalidationEvents() {
        return Res.ok(invalidationListener.recentEvents());
    }

    /// 查询 demo 缓存在本节点 L1 的当前状态(key 集合)
    ///
    /// 从 [org.springframework.cache.CacheManager] 取出缓存实例, 其本地层就是 Caffeine,
    /// 直接读取 key 视图展示——失效广播到达前后对比可看到条目消失
    @Operation(summary = "查询 L1 本地缓存状态")
    @GetMapping("/l1-status")
    public Result<List<CacheL1StatusResult>> l1Status() {
        List<CacheL1StatusResult> statusList = Stream.of(
                        CacheDemoService.PRODUCT_CACHE, CacheDemoService.PRODUCT_LIST_CACHE)
                .map(this::queryL1Status)
                .toList();
        return Res.ok(statusList);
    }

    /// 查询单个缓存名的 L1 状态
    private CacheL1StatusResult queryL1Status(String cacheName) {
        org.springframework.cache.Cache cache = this.cacheManager.getCache(cacheName);
        Object nativeCache = cache == null ? null : cache.getNativeCache();
        if (nativeCache instanceof com.github.benmanes.caffeine.cache.Cache<?, ?> caffeine) {
            List<String> keys = caffeine.asMap()
                    .keySet()
                    .stream()
                    .map(String::valueOf)
                    .sorted()
                    .toList();
            return new CacheL1StatusResult().setCacheName(cacheName).setKeys(keys).setSize(keys.size());
        }
        return new CacheL1StatusResult().setCacheName(cacheName).setKeys(List.of()).setSize(0);
    }
}

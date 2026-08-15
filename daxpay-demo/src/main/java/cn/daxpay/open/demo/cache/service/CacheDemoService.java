package cn.daxpay.open.demo.cache.service;

import cn.daxpay.open.demo.cache.model.CacheDemoProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/// # 缓存读写演示服务
///
/// 通过 [Cacheable] / [CacheEvict] 演示二级缓存(L1 Caffeine + L2 Redis)的读写与失效,
/// 值类型已注册定型序列化([cn.daxpay.open.demo.cache.DemoCacheValueTypeContributor]),
/// L2 命中可还原真实类型。
///
/// 命中观测设计:
/// - 方法体内 sleep 300ms 模拟慢查询, 命中缓存时耗时降到毫秒级, 差异可直接观测
/// - [AtomicInteger] 计数器只统计方法体真实执行次数(即未命中次数),
///   连续读取计数不涨 = 缓存命中
@Slf4j
@Service
public class CacheDemoService {

    /// 单对象缓存名
    public static final String PRODUCT_CACHE = "demo:cache-product";

    /// 列表缓存名(List<POJO> 泛型容器, 定型序列化的重点场景)
    public static final String PRODUCT_LIST_CACHE = "demo:cache-product-list";

    /// mock 商品数据(模拟数据库, 可被演示修改, 故用可变 Map)
    private static final Map<String, CacheDemoProduct> PRODUCT_DB = new ConcurrentHashMap<>(Map.of(
            "P001", new CacheDemoProduct().setId(193456789012345001L).setCode("P001").setName("冰美式咖啡")
                    .setCategory("drink").setPrice(new BigDecimal("18.00"))
                    .setCreateTime(OffsetDateTime.of(2026, 1, 15, 9, 0, 0, 0, ZoneOffset.ofHours(8))),
            "P002", new CacheDemoProduct().setId(193456789012345002L).setCode("P002").setName("拿铁")
                    .setCategory("drink").setPrice(new BigDecimal("22.50"))
                    .setCreateTime(OffsetDateTime.of(2026, 2, 20, 10, 30, 0, 0, ZoneOffset.ofHours(8))),
            "P003", new CacheDemoProduct().setId(193456789012345003L).setCode("P003").setName("柠檬茶")
                    .setCategory("drink").setPrice(new BigDecimal("15.00"))
                    .setCreateTime(OffsetDateTime.of(2026, 3, 8, 14, 0, 0, 0, ZoneOffset.ofHours(8))),
            "F001", new CacheDemoProduct().setId(193456789012345004L).setCode("F001").setName("鸡排饭")
                    .setCategory("food").setPrice(new BigDecimal("28.00"))
                    .setCreateTime(OffsetDateTime.of(2026, 4, 12, 11, 45, 0, 0, ZoneOffset.ofHours(8))),
            "F002", new CacheDemoProduct().setId(193456789012345005L).setCode("F002").setName("牛肉面")
                    .setCategory("food").setPrice(new BigDecimal("32.00"))
                    .setCreateTime(OffsetDateTime.of(2026, 5, 25, 12, 15, 0, 0, ZoneOffset.ofHours(8)))));

    /// 单对象方法体真实执行次数(未命中次数)
    private final AtomicInteger productLoads = new AtomicInteger();

    /// 列表方法体真实执行次数(未命中次数)
    private final AtomicInteger listLoads = new AtomicInteger();

    /// 按编码加载商品(单对象缓存)
    ///
    /// 不存在的编码返回 null 且不缓存(由 disableCachingNullValues 保证), 每次读取都是未命中
    @Cacheable(value = PRODUCT_CACHE, key = "#code")
    public CacheDemoProduct loadProduct(String code) {
        this.productLoads.incrementAndGet();
        this.mockSlowQuery();
        return PRODUCT_DB.get(code);
    }

    /// 按分类加载商品列表(List<POJO> 泛型容器缓存, 演示重点)
    @Cacheable(value = PRODUCT_LIST_CACHE, key = "#category")
    public List<CacheDemoProduct> loadProductList(String category) {
        this.listLoads.incrementAndGet();
        this.mockSlowQuery();
        return PRODUCT_DB.values().stream()
                .filter(p -> p.getCategory().equals(category))
                .toList();
    }

    /// 失效单对象缓存
    @CacheEvict(value = PRODUCT_CACHE, key = "#code")
    public void evictProduct(String code) {
        log.info("演示: 失效单对象缓存, code={}", code);
    }

    /// 失效列表缓存
    @CacheEvict(value = PRODUCT_LIST_CACHE, key = "#category")
    public void evictProductList(String category) {
        log.info("演示: 失效列表缓存, category={}", category);
    }

    /// 修改商品名称并触发缓存失效广播(演示: 修改内容 → L1 集群失效通知)
    ///
    /// 一次修改触发两种广播类型, 供前端事件流观察:
    /// - 单对象缓存按 key 精确失效 → 广播 EVICT(demo:cache-product, code)
    /// - 列表缓存 key 是分类, 无法由商品编码推导, 采用 allEntries 全量失效 → 广播 CLEAR(demo:cache-product-list);
    ///   真实业务中若能从更新数据推导出所属分类, 也可改为精确失效
    ///
    /// 数据替换采用"新实例整体替换"而非修改字段: L1 缓存持有的是旧对象引用,
    /// 替换后缓存里是旧值、mock 库里是新值, 与真实数据库行更新的语义一致——
    /// 未失效的旧缓存会继续返回旧名称, 直到失效广播到达(或 L1 TTL 60 秒兜底过期)。
    ///
    /// @return 更新后的商品; 编码不存在返回 null(不存在的编码仍会触发失效注解, 无害)
    @Caching(evict = {
            @CacheEvict(value = PRODUCT_CACHE, key = "#code"),
            @CacheEvict(value = PRODUCT_LIST_CACHE, allEntries = true)
    })
    public CacheDemoProduct updateProduct(String code, String name) {
        log.info("演示: 修改商品并触发缓存失效广播, code={}, name={}", code, name);
        CacheDemoProduct old = PRODUCT_DB.get(code);
        if (old == null) {
            return null;
        }
        CacheDemoProduct updated = new CacheDemoProduct()
                .setId(old.getId())
                .setCode(old.getCode())
                .setName(name)
                .setCategory(old.getCategory())
                .setPrice(old.getPrice())
                .setCreateTime(old.getCreateTime());
        PRODUCT_DB.put(code, updated);
        return updated;
    }

    /// 单对象方法体真实执行次数
    public int getProductLoads() {
        return this.productLoads.get();
    }

    /// 列表方法体真实执行次数
    public int getListLoads() {
        return this.listLoads.get();
    }

    /// 模拟慢查询(300ms), 使命中/未命中的耗时差异可直接观测
    private void mockSlowQuery() {
        try {
            Thread.sleep(300);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

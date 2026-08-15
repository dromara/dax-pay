package cn.daxpay.open.demo.cache.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/// # 缓存演示商品
///
/// 覆盖典型序列化敏感字段: Long 雪花 id(序列化为字符串)、BigDecimal 金额、OffsetDateTime 时间,
/// 用于验证 L2 定型序列化 round-trip 后类型与值全部保真。
@Data
@Accessors(chain = true)
public class CacheDemoProduct {

    /// 商品 id(模拟雪花 id, 超 int 精度)
    private Long id;

    /// 商品编码
    private String code;

    /// 商品名称
    private String name;

    /// 分类编码
    private String category;

    /// 价格
    private BigDecimal price;

    /// 上架时间
    private OffsetDateTime createTime;
}

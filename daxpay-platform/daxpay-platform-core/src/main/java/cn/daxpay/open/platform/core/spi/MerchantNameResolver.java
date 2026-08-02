package cn.daxpay.open.platform.core.spi;

import java.util.Collection;
import java.util.Map;

/// # 商户名称解析器 SPI
///
/// 由 payment 模块实现, platform 模块(敏感词命中/统一支付日志等不依赖 payment 的模块)
/// 通过此接口将商户号翻译为商户名称.
/// 当系统中不存在 payment 模块时, 翻译功能自动跳过(调用方做 null 防御)
public interface MerchantNameResolver {

    /// 批量解析商户号 -> 商户名称(忽略租户, 运营端跨租户查看)
    ///
    /// @param mchNos 商户号集合
    /// @return mchNo -> mchName 映射, 无对应记录的 mchNo 不放入 map
    Map<String, String> resolveNames(Collection<String> mchNos);
}

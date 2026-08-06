package cn.daxpay.open.payment.strategy.risk;

import cn.hutool.core.util.StrUtil;

/// # 地理围栏工具
///
/// 门店城市与 IP 归属城市的名称归一化, 供 [cn.daxpay.open.payment.trade.runtime.service.pay.common.PayRiskAssistService]
/// 预算放行城市集合与 [cn.daxpay.open.plugin.risk.strategy.DefaultPayRiskChecker] 比对时复用, 保证两侧口径一致。
/// 归一化口径与 [cn.daxpay.open.platform.capability.audit.log.service.ip2region.IpRegion] 内私有 normalizeName 保持一致。
public final class GeoFenceUtil {

    /// 行政区划后缀(由长到短), 迭代去除
    private static final String[] REGION_SUFFIXES =
            {"特别行政区", "维吾尔自治区", "回族自治区", "壮族自治区", "自治区", "省", "市"};

    private GeoFenceUtil() {
    }

    /// 行政区划名称归一化: 迭代去掉省/市/自治区/特别行政区等常见后缀, 用于门店城市与 IP 归属城市对齐比对
    ///
    /// 覆盖: 直辖市(北京市→北京)、普通地级市(深圳市→深圳)、自治区(内蒙古自治区→内蒙古)。
    /// 自治州/盟保留全名(已知限制)。
    public static String normalizeRegionName(String name) {
        if (StrUtil.isBlank(name)) {
            return "";
        }
        String result = name.trim();
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String suffix : REGION_SUFFIXES) {
                if (result.endsWith(suffix)) {
                    result = result.substring(0, result.length() - suffix.length());
                    changed = true;
                    break;
                }
            }
        }
        return result;
    }
}

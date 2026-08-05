package cn.daxpay.open.payment.trade.util;

import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/// # 币种金额换算工具
///
/// 统一处理「元(主币种单位) ↔ 最小货币单位(分/cent等)」的换算,
/// 按 [CurrencyEnum#getMinorUnit] 决定放大/缩小倍数。
///
/// - 内部金额一律使用 Long + 最小货币单位(与 DB `amount int8` 一致)
/// - 通道金额: Stripe/Adyen 等国际通道也用最小货币单位, 可直接透传
/// - 展示金额: 元形式(BigDecimal), 按 minorUnit 还原
///
/// 例:
/// - CNY(minorUnit=2): 1 元 = 100 分, 金额 100L ↔ "1.00"
/// - JPY(minorUnit=0): 1 元 = 1, 金额 1L ↔ "1"
/// - USD(minorUnit=2): $1.00 = 100 cents, 金额 100L ↔ "1.00"
public final class CurrencyAmountUtil {

    private CurrencyAmountUtil() {
    }

    /// 元(BigDecimal) → 最小货币单位(Long)
    ///
    /// @param majorAmount 元形式金额(如 1.50 表示 1 元 5 角 / $1.50)
    /// @param currency    币种枚举
    /// @return 最小货币单位(如 CNY/USD 返回 150, JPY 返回 2)
    public static long majorToMinor(BigDecimal majorAmount, CurrencyEnum currency) {
        if (majorAmount == null) {
            return 0L;
        }
        Objects.requireNonNull(currency, "币种不能为空");
        BigDecimal minor = majorAmount.scaleByPowerOfTen(currency.getMinorUnit());
        return minor.setScale(0, RoundingMode.HALF_UP).longValueExact();
    }

    /// 元(字符串) → 最小货币单位(Long)
    public static long majorToMinor(String majorAmountStr, CurrencyEnum currency) {
        if (majorAmountStr == null || majorAmountStr.isBlank()) {
            return 0L;
        }
        return majorToMinor(new BigDecimal(majorAmountStr), currency);
    }

    /// 最小货币单位(Long) → 元(BigDecimal)
    ///
    /// @param minorAmount 最小货币单位(如 150)
    /// @param currency    币种枚举
    /// @return 元形式(如 CNY/USD 返回 1.50, JPY 返回 150)
    public static BigDecimal minorToMajor(long minorAmount, CurrencyEnum currency) {
        Objects.requireNonNull(currency, "币种不能为空");
        BigDecimal major = BigDecimal.valueOf(minorAmount)
                .scaleByPowerOfTen(-currency.getMinorUnit());
        return major.setScale(currency.getMinorUnit(), RoundingMode.HALF_UP);
    }

    /// 最小货币单位(Long) → 元(字符串, 保留 minorUnit 位小数)
    public static String minorToMajorStr(long minorAmount, CurrencyEnum currency) {
        return minorToMajor(minorAmount, currency).toPlainString();
    }

    /// 按币种 code 获取 minorUnit(便捷方法, 未知币种抛 [DataNotExistException])
    public static int minorUnitOf(String currencyCode) {
        return CurrencyEnum.findByCode(currencyCode).getMinorUnit();
    }

    /// 校验两个币种是否一致(不一致抛异常由调用方包装)
    public static boolean sameCurrency(String code1, String code2) {
        if (code1 == null || code2 == null) {
            return false;
        }
        return code1.equalsIgnoreCase(code2);
    }
}

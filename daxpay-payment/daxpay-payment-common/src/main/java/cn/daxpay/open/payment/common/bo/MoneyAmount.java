package cn.daxpay.open.payment.common.bo;

import cn.daxpay.open.platform.core.enums.pay.channel.CurrencyEnum;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Currency;
import java.util.Objects;

/// # 金额值对象
///
/// 封装金额与币种，统一运算与展示，杜绝散落换算
///
/// @param amount   金额（该 currency 的最小货币单位：CNY 分 / USD cent / JPY 元）
/// @param currency ISO 4217 币种代码，默认 CNY
public record MoneyAmount(Long amount, String currency) {

    public static final String DEFAULT_CURRENCY = CurrencyEnum.CNY.getCode();

    public MoneyAmount {
        currency = Objects.requireNonNullElse(currency, DEFAULT_CURRENCY);
    }

    /// 使用默认币种 CNY
    public MoneyAmount(Long amount) {
        this(amount, DEFAULT_CURRENCY);
    }

    /// 零金额
    public static MoneyAmount zero() {
        return new MoneyAmount(0L, DEFAULT_CURRENCY);
    }

    public static MoneyAmount zero(String currency) {
        return new MoneyAmount(0L, currency);
    }

    /// 转换为展示金额字符串
    /// 按 currency 的小数位数换算，保留对应精度
    public String toDisplayString() {
        Currency javaCurrency = Currency.getInstance(currency);
        int fractionDigits = javaCurrency.getDefaultFractionDigits();
        BigDecimal displayAmount = BigDecimal.valueOf(amount)
                .divide(BigDecimal.valueOf(Math.pow(10, fractionDigits)), fractionDigits, RoundingMode.HALF_UP);
        DecimalFormat df = new DecimalFormat("#,##0." + "#".repeat(Math.max(0, fractionDigits)));
        return df.format(displayAmount) + " " + currency;
    }

    /// 添加金额（同币种）
    public MoneyAmount add(MoneyAmount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("币种不一致: " + this.currency + " vs " + other.currency);
        }
        return new MoneyAmount(this.amount + other.amount, this.currency);
    }

    /// 减去金额（同币种）
    public MoneyAmount subtract(MoneyAmount other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("币种不一致: " + this.currency + " vs " + other.currency);
        }
        return new MoneyAmount(this.amount - other.amount, this.currency);
    }

    /// 转换为 BigDecimal 主单位（元/美元等，用于展示）
    public BigDecimal toBigDecimal() {
        Currency javaCurrency = Currency.getInstance(currency);
        int fractionDigits = javaCurrency.getDefaultFractionDigits();
        return BigDecimal.valueOf(amount)
                .divide(BigDecimal.valueOf(Math.pow(10, fractionDigits)), fractionDigits, RoundingMode.HALF_UP);
    }

    /// 获取该币种的小数位数
    public int getFractionDigits() {
        return Currency.getInstance(currency).getDefaultFractionDigits();
    }
}

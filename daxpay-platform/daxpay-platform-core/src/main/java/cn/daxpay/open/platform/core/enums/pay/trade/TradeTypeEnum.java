package cn.daxpay.open.platform.core.enums.pay.trade;

import cn.daxpay.open.platform.core.exception.config.ConfigNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 交易类型, 如支付/退款/转账等
///
/// 字典: trade_type
@Getter
@RequiredArgsConstructor
public enum TradeTypeEnum implements I18nSupport {

    /// 支付
    PAY("pay"),
    /// 提现
    CASHOUTS("cashouts"),
    /// 结算
    SETTLE("settle"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.trade_type";
    }

    public static TradeTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(tradeTypeEnum -> tradeTypeEnum.getCode().equals(code))
                .findFirst()
                // 通用: 交易类型不存在: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.tradeTypeNotExist", code));
    }
}

package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 资金交易形态
///
/// 标识 pay_trade 表中每笔资金交易的具体形态/动作类型
/// 字典: pay_trade_type
@Getter
@RequiredArgsConstructor
public enum PayTradeTypeEnum implements I18nSupport {

    /// 普通支付
    NORMAL("normal"),
    /// 预授权冻结
    AUTHORIZE("authorize"),
    /// 预授权捕获扣款
    CAPTURE("capture"),
    /// 周期代扣
    RECURRING("recurring"),
    /// 合单子单支付
    COMBINE_SUB("combine_sub"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_trade_type";
    }

    public static PayTradeTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.payTradeTypeNotExist", code));
    }
}

package cn.daxpay.open.payment.trade.abnormal.enums;

import cn.daxpay.open.payment.trade.enums.PayFundStatusEnum;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

/// # 异常订单类型
///
/// 终态订单收到通道收款证据时的异常归类, 与发现时资金状态一一对应
/// 字典: abnormal_type
@Getter
@RequiredArgsConstructor
public enum AbnormalOrderTypeEnum implements I18nSupport {

    /// 关单后收款(终态 close, 通道实际已付款)
    CLOSE_PAID("close_paid"),
    /// 失败后收款(终态 fail, 通道实际已付款)
    FAIL_PAID("fail_paid"),
    /// 撤销后收款(终态 cancel, 通道实际已付款)
    CANCEL_PAID("cancel_paid"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.abnormal_type";
    }

    /// 按资金终态解析异常类型(close/fail/cancel)
    public static AbnormalOrderTypeEnum fromTradeStatus(String tradeStatus) {
        if (Objects.equals(tradeStatus, PayFundStatusEnum.CLOSE.getCode())) {
            return CLOSE_PAID;
        }
        if (Objects.equals(tradeStatus, PayFundStatusEnum.FAIL.getCode())) {
            return FAIL_PAID;
        }
        if (Objects.equals(tradeStatus, PayFundStatusEnum.CANCEL.getCode())) {
            return CANCEL_PAID;
        }
        throw new IllegalArgumentException("非异常订单资金状态: " + tradeStatus);
    }
}

package cn.daxpay.open.payment.trade.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 普通支付业务单状态
///
/// pay_normal_order 容器的业务状态
/// 字典: normal_order_status
@Getter
@RequiredArgsConstructor
public enum NormalPayOrderStatusEnum implements I18nSupport {

    /// 待支付
    WAIT_PAY("wait_pay"),
    /// 已支付
    PAID("paid"),
    /// 支付失败（资金态 fail，与主动关单 closed 区分）
    FAILED("failed"),
    /// 已关闭（主动关单/撤销）
    CLOSED("closed"),
    /// 已过期（超时关单）
    EXPIRED("expired"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.normal_order_status";
    }

    public static NormalPayOrderStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.normalOrderStatusNotExist", code));
    }
}

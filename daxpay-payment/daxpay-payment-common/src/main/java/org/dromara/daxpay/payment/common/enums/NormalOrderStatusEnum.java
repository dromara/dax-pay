package org.dromara.daxpay.payment.common.enums;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 普通支付业务单状态
///
/// pay_normal_order 容器的业务状态
/// 字典: normal_order_status
@Getter
@RequiredArgsConstructor
public enum NormalOrderStatusEnum implements I18nSupport {

    /// 待支付
    WAIT_PAY("wait_pay"),
    /// 已支付
    PAID("paid"),
    /// 已关闭
    CLOSED("closed"),
    /// 已过期
    EXPIRED("expired"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.normal_order_status";
    }

    public static NormalOrderStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.normalOrderStatusNotExist", code));
    }
}

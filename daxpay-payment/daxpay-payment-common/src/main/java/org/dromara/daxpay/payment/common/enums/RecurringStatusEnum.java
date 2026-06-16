package org.dromara.daxpay.payment.common.enums;

import org.dromara.daxpay.platform.core.exception.DataNotExistException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 周期签约状态
///
/// pay_recurring 容器的签约状态
/// 字典: recurring_status
@Getter
@RequiredArgsConstructor
public enum RecurringStatusEnum implements I18nSupport {

    /// 已签约
    SIGNED("signed"),
    /// 扣款中
    EXECUTING("executing"),
    /// 已完成（计划完成）
    COMPLETED("completed"),
    /// 已解约
    CANCELED("canceled"),
    /// 签约过期
    EXPIRED("expired"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.recurring_status";
    }

    public static RecurringStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.recurringStatusNotExist", code));
    }
}

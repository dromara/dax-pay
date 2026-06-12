package org.dromara.daxpay.platform.core.enums.pay.pay;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 支付订单的分账状态
///
/// 字典: pay_alloc_status
@Getter
@RequiredArgsConstructor
public enum PayAllocStatusEnum implements I18nSupport {

    /// 待分账
    WAITING("waiting"),
    /// 已分账
    ALLOCATION("allocation");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.pay_alloc_status";
    }

}

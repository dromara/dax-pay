package org.dromara.daxpay.platform.core.enums.pay.trade;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 结算状态
///
/// 字典: settle_status
@Getter
@RequiredArgsConstructor
public enum SettleStatusEnum implements I18nSupport {

    /// 未结算
    NOT_SETTLE("not_settle"),
    /// 已结算
    SETTLED("settled");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.settle_status";
    }

}

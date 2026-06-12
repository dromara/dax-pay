package org.dromara.daxpay.platform.core.enums.pay.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 支付订单关闭类型
///
/// 字典值 close_type
@Getter
@AllArgsConstructor
public enum CloseTypeEnum {

    /// 关闭
    CLOSE("close", PayStatusEnum.CLOSE),
    /// 撤销
    CANCEL("cancel", PayStatusEnum.CANCEL),
    ;

    private final String code;
    private final PayStatusEnum payStatus;
}

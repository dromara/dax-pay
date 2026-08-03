package cn.daxpay.open.platform.core.enums.pay.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 支付回调处理状态
///
/// 字典: callback_status
@Getter
@AllArgsConstructor
public enum CallbackStatusEnum {
    /// 成功
    SUCCESS("success"),
    /// 失败
    FAIL("fail"),
    /// 关闭(退款关闭, 与失败区分: 资金均回滚, 但状态语义不同)
    CLOSE("close"),
    /// 忽略
    IGNORE("ignore"),
    /// 异常
    EXCEPTION("exception"),
    /// 未找到
    NOT_FOUND("not_found");

    private final String code;
}

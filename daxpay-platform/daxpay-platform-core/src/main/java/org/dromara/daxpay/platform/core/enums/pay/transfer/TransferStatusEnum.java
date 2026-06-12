package org.dromara.daxpay.platform.core.enums.pay.transfer;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 转账状态
///
/// 字典: transfer_status
@Getter
@RequiredArgsConstructor
public enum TransferStatusEnum implements I18nSupport {

    /// 转账中
    PROGRESS("progress"),
    /// 转账成功
    SUCCESS("success"),
    /// 转账关闭
    CLOSE("close"),
    /// 转账失败
    FAIL("fail"),
    ;

    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.transfer_status";
    }
}

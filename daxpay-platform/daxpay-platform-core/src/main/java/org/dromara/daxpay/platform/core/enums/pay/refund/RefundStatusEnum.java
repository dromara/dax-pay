package org.dromara.daxpay.platform.core.enums.pay.refund;

import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 退款状态枚举
///
/// 字典: refund_status
@Getter
@RequiredArgsConstructor
public enum RefundStatusEnum implements I18nSupport {

    /// 接口调用成功不代表成功
    PROGRESS("progress"),
    /// 退款成功
    SUCCESS("success"),
    /// 退款关闭
    CLOSE("close"),
    /// 退款失败
    FAIL("fail");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.refund_status";
    }

    public static RefundStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.code, code))
                .findFirst()
                // 退款状态不存在: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.refundStatusNotExist", code));
    }

}

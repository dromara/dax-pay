package org.dromara.daxpay.platform.core.enums.pay.trade;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import java.util.Arrays;

/// # 交易状态
///
/// 字典: trade_status
@Getter
@RequiredArgsConstructor
public enum TradeStatusEnum implements I18nSupport {

    /// 执行中
    PROGRESS("progress"),
    /// 成功
    SUCCESS("success"),
    /// 失败
    FAIL("fail"),
    /// 关闭
    CLOSED("closed"),
    /// 撤销
    REVOKED("revoked"),
    /// 异常
    EXCEPTION("exception");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.trade_status";
    }
    public static TradeStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                // 交易状态不存在: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.tradeStatusNotExist", code));
    }

}

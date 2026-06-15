package org.dromara.daxpay.platform.core.enums.pay.reconcile;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.dromara.daxpay.platform.core.exception.config.ConfigNotEnableException;
import java.util.Arrays;
import java.util.Objects;

/// # 对账单文件
///
@Getter
@RequiredArgsConstructor
public enum ReconcileFileTypeEnum implements I18nSupport {

    /// 交易对账单
    TRADE("trade"),
    /// 压缩包
    ZIP("zip");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.reconcile_file_type";
    }
    public static ReconcileFileTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                // 通用: 未知的对账文件类型: {0}
                .orElseThrow(() -> new ConfigNotEnableException("error.common.reconcileFileTypeUnknown", code));
    }

}

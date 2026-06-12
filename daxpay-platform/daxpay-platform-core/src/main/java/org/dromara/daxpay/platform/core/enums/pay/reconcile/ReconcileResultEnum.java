package org.dromara.daxpay.platform.core.enums.pay.reconcile;

import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 对账结果
///
/// 字典: reconcile_result
@Getter
@RequiredArgsConstructor
public enum ReconcileResultEnum implements I18nSupport {

    /// 一致
    CONSISTENT("consistent"),

    /// 不一致
    INCONSISTENT("inconsistent");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.reconcile_result";
    }

    public static ReconcileResultEnum findByCode(String code) {
        return Arrays.stream(ReconcileResultEnum.values())
                .filter(value -> value.getCode().equals(code))
                .findFirst()
                // 未找到对应的对账结果: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.reconcileResultNotFound", code));
    }
}

package org.dromara.daxpay.platform.core.enums.pay.reconcile;

import org.dromara.daxpay.platform.core.exception.config.ConfigNotExistException;
import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 对账差异类型
///
/// 字典: reconcile_discrepancy_type
@Getter
@RequiredArgsConstructor
public enum ReconcileDiscrepancyTypeEnum implements I18nSupport {

    /// 一致
    CONSISTENT("consistent"),
    /// 本地订单不存在
    LOCAL_NOT_EXISTS("local_not_exists"),
    /// 远程订单不存在
    REMOTE_NOT_EXISTS("remote_not_exists"),
    /// 订单信息不一致
    NOT_MATCH("not_match");

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.reconcile_discrepancy_type";
    }

    public static ReconcileDiscrepancyTypeEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(value -> Objects.equals(value.getCode(), code))
                .findFirst()
                // 未找到对应的支付类型: {0}
                .orElseThrow(() -> new ConfigNotExistException("error.common.reconcileDiscrepancyTypeNotFound", code));
    }
}

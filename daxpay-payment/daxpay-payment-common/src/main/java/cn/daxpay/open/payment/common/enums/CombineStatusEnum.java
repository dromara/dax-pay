package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 合单整体状态
///
/// pay_combine 容器的聚合状态（由子单 pay 状态聚合计算）
/// 字典: combine_status
@Getter
@RequiredArgsConstructor
public enum CombineStatusEnum implements I18nSupport {

    /// 待支付
    WAIT_PAY("wait_pay"),
    /// 部分成功
    PARTIAL_SUCCESS("partial_success"),
    /// 全部成功
    ALL_SUCCESS("all_success"),
    /// 已关闭
    CLOSED("closed"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.combine_status";
    }

    public static CombineStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.combineStatusNotExist", code));
    }
}

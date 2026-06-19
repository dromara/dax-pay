package cn.daxpay.open.platform.core.enums.pay.pay;

import cn.daxpay.open.platform.core.exception.system.StatusNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/// # 支付订单的退款状态
///
/// 字典: pay_refund_status
@Getter
@RequiredArgsConstructor
public enum PayRefundStatusEnum implements I18nSupport {
    /// 未退款
    NO_REFUND("no_refund"),
    /// 退款中
    REFUNDING("refunding"),
    /// 部分退款
    PARTIAL_REFUND("partial_refund"),
    /// 全部退款
    REFUNDED("refunded"),
    ;
    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.pay_refund_status";
    }

    /// 根据编码获取枚举
    public static PayRefundStatusEnum findByCode(String code){
        return Arrays.stream(values())
                .filter(payStatusEnum -> Objects.equals(payStatusEnum.getCode(), code))
                .findFirst()
                // 通用: 该退款状态不存在: {0}
                .orElseThrow(() -> new StatusNotExistException("error.common.payRefundStatusNotExist", code));
    }

}

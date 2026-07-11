package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 网关支付业务单状态
///
/// 字典: gateway_order_status
@Getter
@RequiredArgsConstructor
public enum GatewayOrderStatusEnum implements I18nSupport {

    /// 待支付(已预下单, 未建 Trade)
    WAIT_PAY("wait_pay"),
    /// 支付中(已建 Trade, 通道处理中)
    PAYING("paying"),
    /// 已支付
    PAID("paid"),
    /// 已关闭
    CLOSED("closed"),
    /// 已过期
    EXPIRED("expired"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.gateway_order_status";
    }

    public static GatewayOrderStatusEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.gatewayOrderStatusNotExist", code));
    }
}

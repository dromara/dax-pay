package cn.daxpay.open.payment.trade.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 网关支付类型
///
/// 字典: gateway_pay_type
@Getter
@RequiredArgsConstructor
public enum GatewayPayTypeEnum implements I18nSupport {

    /// 统一收银台(设备自适应, 本期仅预留下单类型)
    CASHIER("cashier"),
    /// 聚合扫码一码多付
    AGGREGATE("aggregate"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.gateway_pay_type";
    }

    public static GatewayPayTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.gatewayPayTypeNotExist", code));
    }
}

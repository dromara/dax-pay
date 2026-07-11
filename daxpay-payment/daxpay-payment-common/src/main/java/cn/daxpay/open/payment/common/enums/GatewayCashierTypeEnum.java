package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 网关收银台配置类型
///
/// 字典: gateway_cashier_type；
/// 用于收银台支付项配置分桶(H5 / WEB), 不是订单 gateway_type。
@Getter
@RequiredArgsConstructor
public enum GatewayCashierTypeEnum implements I18nSupport {

    /// H5 收银台(按终端 scene 再分桶)
    H5("h5"),
    /// WEB/PC 收银台(扁平列表, scene 为空)
    WEB("web"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.gateway_cashier_type";
    }

    public static GatewayCashierTypeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.gatewayCashierTypeNotExist", code));
    }
}

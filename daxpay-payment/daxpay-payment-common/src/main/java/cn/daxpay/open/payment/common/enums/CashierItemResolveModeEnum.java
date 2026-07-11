package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 收银台支付项解析模式
///
/// 字典: cashier_item_resolve_mode；
/// 按支付项控制支付解析方式(无 auto, 收银台由用户点选):
/// - METHOD: 配置支付方式, 走路由场景模式
/// - DIRECT: 配置通道商户号+支付能力, 走路由直定模式
@Getter
@RequiredArgsConstructor
public enum CashierItemResolveModeEnum implements I18nSupport {

    /// 方式模式: 配置支付方式
    METHOD("method"),
    /// 精确模式: 配置通道商户号+支付能力
    DIRECT("direct"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.cashier_item_resolve_mode";
    }

    public static CashierItemResolveModeEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.cashierItemResolveModeNotExist", code));
    }
}

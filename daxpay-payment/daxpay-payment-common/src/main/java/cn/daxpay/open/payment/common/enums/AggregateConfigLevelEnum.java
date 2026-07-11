package cn.daxpay.open.payment.common.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 聚合扫码配置深度
///
/// 字典: aggregate_config_level；
/// 控制聚合扫码支付时, 场景(微信/支付宝/云闪付/抖音)解析支付方式的自由度:
/// - AUTO: 自动模式, 系统按扫码环境推导支付方式, 走路由基础模式解析
/// - METHOD: 方式模式, 每个场景手动配置支付方式, 走路由场景模式解析
/// - DIRECT: 精确模式, 每个场景直接指定通道商户号+支付能力, 走路由直定模式
@Getter
@RequiredArgsConstructor
public enum AggregateConfigLevelEnum implements I18nSupport {

    /// 自动模式: 系统按扫码环境推导支付方式
    AUTO("auto"),
    /// 方式模式: 每个场景配置支付方式
    METHOD("method"),
    /// 精确模式: 每个场景指定通道商户号+支付能力
    DIRECT("direct"),
    ;

    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.aggregate_config_level";
    }

    public static AggregateConfigLevelEnum findByCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new DataNotExistException("error.common.aggregateConfigLevelNotExist", code));
    }
}

package cn.daxpay.open.payment.merchant.enums;

import cn.daxpay.open.platform.core.exception.DataNotExistException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 聚合扫码配置深度
///
/// 字典: aggregate_config_level；
/// 控制聚合扫码/码牌支付时, 打开环境解析支付方式的自由度:
/// - AUTO: 跟随通道路由, 系统按打开环境推导支付方式后走应用路由
/// - METHOD: 指定支付方式, 按需配置打开环境的支付方式(配多少用多少)后走应用路由
/// - DIRECT: 直接指定, 按需配置通道商户号+支付能力(配多少用多少), 跳过应用路由
@Getter
@RequiredArgsConstructor
public enum AggregateConfigLevelEnum implements I18nSupport {

    /// 跟随通道路由: 系统按打开环境推导支付方式
    AUTO("auto"),
    /// 指定支付方式: 每个打开环境配置支付方式
    METHOD("method"),
    /// 直接指定: 每个打开环境配置通道商户号+支付能力
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

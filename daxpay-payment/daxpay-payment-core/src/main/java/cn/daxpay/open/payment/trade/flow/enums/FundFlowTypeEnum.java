package cn.daxpay.open.payment.trade.flow.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 资金流水类型
///
/// 字典: fund_flow_type
@Getter
@RequiredArgsConstructor
public enum FundFlowTypeEnum implements I18nSupport {

    /// 收款(支付成功入账)
    PAY("pay"),
    /// 退款(退款成功支出)
    REFUND("refund"),
    ;

    /// 编码
    private final String code;

    /// 根据编码获取枚举, 未知编码返回 null
    ///
    /// 与 [cn.daxpay.open.platform.core.enums.pay.channel.ChannelEnum#findByCode] 等保持一致的 null 容忍策略:
    /// 导出/展示场景可能遇到历史数据或后续新增类型, 单行未知值不应打崩整批处理。
    public static FundFlowTypeEnum findByCode(String code) {
        for (FundFlowTypeEnum value : values()) {
            if (value.code.equals(code)) return value;
        }
        return null;
    }

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.fund_flow_type";
    }
}

package cn.daxpay.open.payment.trade.abnormal.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 异常订单处理状态
///
/// 字典: abnormal_handle_status
@Getter
@RequiredArgsConstructor
public enum AbnormalHandleStatusEnum implements I18nSupport {

    /// 待处理
    PENDING("pending"),
    /// 已确认成功(人工核实通道已收款, 订单翻转为 SUCCESS)
    CONFIRMED("confirmed"),
    /// 已忽略(核实无需入账, 如通道侧已原路退回)
    IGNORED("ignored"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.abnormal_handle_status";
    }
}

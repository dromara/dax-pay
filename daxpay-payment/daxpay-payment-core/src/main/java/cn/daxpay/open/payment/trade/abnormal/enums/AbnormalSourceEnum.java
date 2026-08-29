package cn.daxpay.open.payment.trade.abnormal.enums;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 异常订单发现来源
///
/// 字典: abnormal_source
@Getter
@RequiredArgsConstructor
public enum AbnormalSourceEnum implements I18nSupport {

    /// 通道回调(成功回调到达时订单已终态)
    CALLBACK("callback"),
    /// 同步查单(商户/管理端手动同步发现通道已收款)
    SYNC("sync"),
    /// 定时任务(FAIL/CLOSE 纠正窗口扫描发现)
    JOB("job"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.abnormal_source";
    }
}

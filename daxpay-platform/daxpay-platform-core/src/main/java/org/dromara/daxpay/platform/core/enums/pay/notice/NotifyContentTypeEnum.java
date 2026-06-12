package org.dromara.daxpay.platform.core.enums.pay.notice;

import org.dromara.daxpay.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 客户通知内容类型
///
/// 字典: notify_content_type
@Getter
@RequiredArgsConstructor
public enum NotifyContentTypeEnum implements I18nSupport {

    /// 支付订单变动通知
    PAY("pay"),
    /// 退款订单变动通知
    REFUND("refund"),
    /// 转账订单变动通知
    TRANSFER("transfer"),
    /// 分账订单变动通知
    ALLOCATION("allocation");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.notify_content_type";
    }

}

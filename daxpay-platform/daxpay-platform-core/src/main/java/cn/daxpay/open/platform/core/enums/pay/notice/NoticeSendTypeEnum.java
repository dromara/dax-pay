package cn.daxpay.open.platform.core.enums.pay.notice;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 消息发送类型
///
/// 字典: notice_send_type
@Getter
@RequiredArgsConstructor
public enum NoticeSendTypeEnum implements I18nSupport {

    /// 自动发送
    AUTO("auto"),
    /// 手动发送
    MANUAL("manual");

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.notice_send_type";
    }

}

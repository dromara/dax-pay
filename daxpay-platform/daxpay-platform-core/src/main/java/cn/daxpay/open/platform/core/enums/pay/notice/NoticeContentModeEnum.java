package cn.daxpay.open.platform.core.enums.pay.notice;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 商户出站通知内容策略
///
/// 字典: notice_content_mode
@Getter
@RequiredArgsConstructor
public enum NoticeContentModeEnum implements I18nSupport {

    /// 注册时冻结 JSON 快照（重试内容稳定、可审计）
    SNAPSHOT("snapshot"),
    /// content 仅存业务指针，发送时实时组装
    REF("ref"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.notice_content_mode";
    }
}

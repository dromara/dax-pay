package cn.daxpay.open.platform.core.enums.pay.notice;

import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/// # 商户出站通知报文格式
///
/// 字典: notice_format
/// 仅描述报文如何组装(JSON / GET query 等)与签名方式, 与传输通道 [NoticeTransportEnum] 正交
/// 演进自原 [NoticeProtocolEnum], 拆分后 protocol 不再一维承担「报文格式 + 传输通道 + 路由」三重职责
@Getter
@RequiredArgsConstructor
public enum NoticeFormatEnum implements I18nSupport {

    /// 标准 DaxPay 签名 JSON 报文
    SYSTEM("system"),
    /// 易支付兼容协议 GET query 报文
    EASY_PAY("easy_pay"),
    ;

    /// 编码
    private final String code;

    /// 翻译 key 前缀
    @Override
    public String getI18nPrefix() {
        return "enum.notice_format";
    }
}

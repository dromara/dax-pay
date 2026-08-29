package cn.daxpay.open.platform.core.code;

/// # web请求头常量
///
public interface WebHeaderCode {

    /// 用户代理
    String USER_AGENT = "user-agent";

    /// 防重放Nonce
    String X_NONCE = "x-nonce";

    /// 防重放时间戳
    String X_TIMESTAMP = "x-timestamp";

    /// 追踪ID
    String X_TRACE_ID = "x-trace-id";

    /// 身份域编码
    String X_CLIENT_CODE = "x-client-code";

    /// 请求终端: web=PC Web端 / app=移动管理端
    String X_TERMINAL = "x-terminal";

    /// 国际化语言
    String ACCEPT_LANGUAGE = "accept-language";

    /// 通道传输报文已 AES-GCM 加密标记（主应用 ↔ 通道子应用）
    String X_DAX_PAYLOAD_ENCRYPTED = "X-Dax-Payload-Encrypted";

}

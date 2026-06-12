package org.dromara.daxpay.platform.core.code;

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

    /// 终端编码
    String X_CLIENT_CODE = "x-client-code";

    /// 国际化语言
    String ACCEPT_LANGUAGE = "accept-language";

}

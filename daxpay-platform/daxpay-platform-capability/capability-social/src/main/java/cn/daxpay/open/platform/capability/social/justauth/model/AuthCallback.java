package cn.daxpay.open.platform.capability.social.justauth.model;

import lombok.Data;
import lombok.experimental.Accessors;

/// # OAuth2 回调参数
///
/// 第三方平台授权后回调时携带的参数
///
@Data
@Accessors(chain = true)
public class AuthCallback {

    /// 授权码
    private String code;

    /// 状态码(防 CSRF)
    private String state;

    /// 原始授权码(部分平台使用 authCode 命名)
    private String authCode;

    /// 从请求参数构建回调对象
    public static AuthCallback of(String code, String state) {
        return new AuthCallback().setCode(code).setState(state);
    }
}

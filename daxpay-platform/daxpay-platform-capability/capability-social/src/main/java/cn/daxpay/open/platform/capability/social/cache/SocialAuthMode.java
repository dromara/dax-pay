package cn.daxpay.open.platform.capability.social.cache;

/// # 社交登录授权场景
///
public enum SocialAuthMode {

    /// 已登录用户主动绑定
    BIND,

    /// 未登录用户使用三方账号直接登录(仅已绑定可登录)
    LOGIN
}

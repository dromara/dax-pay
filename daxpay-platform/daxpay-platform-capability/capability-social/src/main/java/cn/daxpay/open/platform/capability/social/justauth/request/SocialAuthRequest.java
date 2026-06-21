package cn.daxpay.open.platform.capability.social.justauth.request;

import cn.daxpay.open.platform.capability.social.justauth.model.AuthCallback;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthToken;
import cn.daxpay.open.platform.capability.social.justauth.model.AuthUser;

/// # 社交授权请求接口
///
/// 参考 JustAuth 的 AuthRequest 重新实现, 定义平台授权登录的标准行为
///
public interface SocialAuthRequest {

    /// 生成授权地址
    /// @param state 防 CSRF 的状态码(由调用方生成并缓存)
    String authorize(String state);

    /// 完整登录流程: 用授权码换取令牌并获取用户信息
    /// @param callback 回调参数(code/state)
    AuthUser login(AuthCallback callback);

    /// 用授权码换取访问令牌
    AuthToken getAccessToken(AuthCallback callback);

    /// 用访问令牌获取用户信息
    AuthUser getUserInfo(AuthToken token);
}

package cn.daxpay.open.platform.capability.social.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/// # 社交登录处理器接口
///
/// capability-social 层在 LOGIN 场景下通过绑定关系确认了用户身份后, 委托 service-iam 完成实际的登录签发,
/// 以保证 session 中的 UserDetail 完整(复用平台既有登录成功流程)
///
public interface SocialLoginHandler {

    /// 使用已确认身份的 userId 完成登录(含 session 填充与登录成功回调), 返回 token
    /// @param userId 本地用户ID
    /// @param clientCode 终端编码
    String login(Long userId, String clientCode, HttpServletRequest request, HttpServletResponse response);
}

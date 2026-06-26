package cn.daxpay.open.platform.capability.auth.service;

import cn.daxpay.open.platform.core.entity.UserDetail;
import jakarta.servlet.http.HttpServletRequest;

/// # 已认证请求访问策略 SPI
///
/// 在路由鉴权通过、对已登录用户执行的访问策略, 允许抛异常阻断请求(如密码过期强制改密)。
/// 与 [RouterCheck] 的区别: RouterCheck 仅做"是否放行"的布尔判断且不应抛异常;
/// AccessPolicy 专用于"已认证但需限制访问"的场景, 允许抛业务异常。
/// 实现由 [cn.daxpay.open.platform.capability.auth.handler.SaRouteHandler] 在鉴权链最前面对已登录用户依次调用。
///
public interface AccessPolicy {

    /// 检查访问策略, 不通过则抛异常阻断请求
    /// @param request    当前请求
    /// @param userDetail 当前登录用户(非空)
    void check(HttpServletRequest request, UserDetail userDetail);
}

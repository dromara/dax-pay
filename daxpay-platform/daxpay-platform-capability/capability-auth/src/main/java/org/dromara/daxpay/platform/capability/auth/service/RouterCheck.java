package org.dromara.daxpay.platform.capability.auth.service;

/// # 路由鉴权检查 SPI 接口
///
/// 【执行契约】
/// - 所有实现类由 {@link org.dromara.daxpay.platform.capability.auth.handler.SaRouteHandler} 在启动时按 sortNo 升序收集并排序。
/// - 请求到达时按 sortNo 顺序依次调用 check()，任意一个返回 true 即放行（SaRouter.stop()）。
/// - 全部返回 false：SaRouteHandler 判断未登录抛 NotLoginException，已登录则抛 RouterCheckException（403）。
///
/// 【实现注意事项】
/// - check() 应为幂等操作，不应抛业务异常（权限不足统一由 SaRouteHandler 处理）。
/// - 仅做"是否放行"的布尔判断，异常信息由 SaRouteHandler 统一格式化。
/// - 需要提前拦截（如内部接口限制管理员）时应使用更小的 sortNo。
///
/// @see org.dromara.daxpay.platform.capability.auth.handler.SaRouteHandler
public interface RouterCheck {

    /// 排序号，值越小越先执行。
    /// 建议值域：Integer.MIN_VALUE ~ 0 ~ Integer.MAX_VALUE。
    /// 已知实现：
    /// - Integer.MIN_VALUE - InternalRouterCheck（内部接口管理员校验）
    /// - 0（默认值）           - PermCodeRouterCheck（权限码注解校验）
    default int sortNo() {
        return 0;
    }

    /// 检查是否通过鉴权
    ///
    /// @param handler Sa-Token 传入的路由处理上下文（通常为 HandlerMethod）
    /// @return true=命中并放行，false=不匹配继续下一个 RouterCheck
    boolean check(Object handler);

}



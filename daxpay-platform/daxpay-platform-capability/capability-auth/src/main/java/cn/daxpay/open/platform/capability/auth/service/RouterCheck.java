package cn.daxpay.open.platform.capability.auth.service;

/// # 路由鉴权检查扩展点
///
/// 由 [cn.daxpay.open.platform.capability.auth.handler.SaRouteHandler] 在启动时按 sortNo 升序收集。
/// 请求到达时按顺序调用 check(), 任意一个返回 true 即放行(SaRouter.stop())。
/// 全部返回 false: 未登录抛 NotLoginException, 已登录抛 RouterCheckException(403)。
///
/// 实现注意:
/// - check() 应幂等, 不要抛业务异常(权限不足由 SaRouteHandler 统一处理)。
/// - 只做「是否放行」的布尔判断。
/// - 需要更早拦截(如内部接口限管理员)时用更小的 sortNo。
///
/// @see cn.daxpay.open.platform.capability.auth.handler.SaRouteHandler
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

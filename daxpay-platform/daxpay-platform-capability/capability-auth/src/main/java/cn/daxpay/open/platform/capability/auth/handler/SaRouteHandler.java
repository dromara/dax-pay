package cn.daxpay.open.platform.capability.auth.handler;

import cn.daxpay.open.platform.common.spring.util.WebServletUtil;
import cn.daxpay.open.platform.capability.auth.exception.RouterCheckException;
import cn.daxpay.open.platform.capability.auth.service.AccessPolicy;
import cn.daxpay.open.platform.capability.auth.service.RouterCheck;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.dev33.satoken.fun.SaFunction;
import cn.dev33.satoken.router.SaRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/// # 路由鉴权统一分发
///
/// 规则: **任一 [RouterCheck] 通过就放行; 全都不通过再拒绝**。
/// - 启动时按 sortNo 升序收集全部 RouterCheck 实现。
/// - 请求进入时:
///   1. 已登录用户先依次执行 [AccessPolicy](如密码过期强制改密), 不通过即抛异常阻断;
///   2. 再遍历 RouterCheck, 任意一个返回 true 即调用 SaRouter.stop() 放行。
/// - 全部未命中:
///   - 未登录 → SecurityUtil.getUserId() 抛 NotLoginException
///   - 已登录 → 记 WARN 并抛 RouterCheckException(403 无权限)
///
/// SaRouter.stop() 只结束 Sa-Token 鉴权阶段, 不终止整个过滤器链。
///
/// @see cn.daxpay.open.platform.capability.auth.service.RouterCheck
/// @see cn.daxpay.open.platform.capability.auth.service.AccessPolicy
@Slf4j
@Component
@RequiredArgsConstructor
public class SaRouteHandler implements InitializingBean {

    private final List<RouterCheck> routerChecks;

    private final List<AccessPolicy> accessPolicies;

    @Override
    public void afterPropertiesSet() {
        // 排序
        routerChecks.sort(Comparator.comparing(RouterCheck::sortNo));
    }

    /// 路由检查处理方法
    ///
    /// @param handler Sa-Token 传入的路由处理上下文
    public SaFunction check(Object handler) {
        return () -> {
            String path = WebServletUtil.getPath();
            // 已登录用户: 先执行访问策略(如密码过期强制改密), 不通过则抛异常阻断
            SecurityUtil.getCurrentUser().ifPresent(user -> {
                for (AccessPolicy policy : accessPolicies) {
                    policy.check(WebServletUtil.getRequest(), user);
                }
            });
            // 遍历所有 RouterCheck，命中即放行（按 sortNo 顺序执行）
            for (RouterCheck routerCheck : routerChecks) {
                if (routerCheck.check(handler)) {
                    log.debug("路由鉴权命中: {} -> {}", path, routerCheck.getClass().getSimpleName());
                    SaRouter.stop();
                    return;
                }
            }
            // 全部未命中：判断未登录 vs 无权限
            if (SecurityUtil.notLogin()) {
                log.debug("路由鉴权未命中（未登录）: {}", path);
                SecurityUtil.getUserId(); // 触发 NotLoginException
            }
            // 已登录但无权限
            log.warn("路由鉴权未命中（已登录无权限）: {}，已检查 {} 个 RouterCheck", path, routerChecks.size());
            throw new RouterCheckException();
        };
    }
}

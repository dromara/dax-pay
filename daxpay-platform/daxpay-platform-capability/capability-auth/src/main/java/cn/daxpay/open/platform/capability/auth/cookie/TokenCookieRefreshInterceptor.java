package cn.daxpay.open.platform.capability.auth.cookie;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.config.SaCookieConfig;
import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaCookie;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/// # 登录态 Cookie 自愈拦截器
///
/// 站内通知的 SSE 端点依赖 `EventSource`, 浏览器不允许它自定义请求头, 只能靠登录时写入的
/// `Accesstoken` 同源 Cookie 透传会话; 而该 Cookie 只在登录那一刻写一次(Sa-Token 不续期),
/// 同 host 下多端(运营/商户)还共用同一个 Cookie 互相覆盖. 一旦它缺失、过期或被覆盖,
/// 表现就是「其余接口都正常(走请求头), 只有 SSE 一直 401」.
///
/// 本拦截器在「请求带了有效 token 但 Cookie 缺失/值不一致」时, 用当前 token 重写一次 Cookie,
/// 让任意一次普通接口调用都能顺手把 SSE 依赖的会话 Cookie 修回来.
///
/// ## 开销
/// 稳态下只做两次内存比较(token 提取 + Cookie 比对), **不查会话**; 仅当 Cookie 异常时才校验登录态.
///
/// ## 顺序
/// 由 [cn.daxpay.open.platform.capability.auth.configuration.SaTokenConfigure] 注册在 Sa-Token
/// 鉴权拦截器之后, 保证 SaHolder 上下文可用; 且不作用于静态资源与未映射路径.
@Slf4j
@Component
public class TokenCookieRefreshInterceptor implements HandlerInterceptor {

    /// token 永不过期/取不到剩余时间时使用的兜底有效期(秒), 与 sa-token.timeout 默认值一致
    private static final long DEFAULT_MAX_AGE = 259_200L;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 未携带 token(匿名接口/静态资源)直接放行, 零成本
        String token = StpUtil.getTokenValue();
        if (token == null || token.isBlank()) {
            return true;
        }
        SaTokenConfig config = SaManager.getConfig();
        String tokenName = config.getTokenName();
        // Cookie 与当前 token 一致(稳态)直接放行, 不触发会话查询
        if (token.equals(readCookie(request, tokenName))) {
            return true;
        }
        // Cookie 缺失/不一致: 仅对有效登录态重写, 失效或伪造的 token 不落 Cookie
        if (!StpUtil.isLogin()) {
            return true;
        }
        SaCookieConfig cookieConfig = config.getCookie();
        SaCookie cookie = new SaCookie()
                .setName(tokenName)
                .setValue(token)
                .setPath(cookieConfig == null || cookieConfig.getPath() == null ? "/" : cookieConfig.getPath())
                .setDomain(cookieConfig == null ? null : cookieConfig.getDomain())
                .setSecure(cookieConfig != null && Boolean.TRUE.equals(cookieConfig.getSecure()))
                // 前端不读取该 Cookie, 统一 HttpOnly 收敛 XSS 暴露面(登录时下发的那份由 yml cookie.http-only 一次性对齐)
                .setHttpOnly(true)
                .setSameSite("Lax")
                .setMaxAge(resolveMaxAge(config));
        SaHolder.getResponse().addCookie(cookie);
        log.debug("登录态 Cookie 已按当前 token 重写: {}", tokenName);
        return true;
    }

    /// 解析 Cookie 有效期: 优先用当前 token 剩余有效期, 保证 Cookie 与会话同时失效
    private int resolveMaxAge(SaTokenConfig config) {
        long maxAge = StpUtil.getTokenTimeout();
        if (maxAge <= 0) {
            Long fallback = config.getTimeout();
            maxAge = fallback == null ? DEFAULT_MAX_AGE : fallback;
        }
        return (int) Math.min(maxAge, Integer.MAX_VALUE);
    }

    /// 读取请求中的指定 Cookie(不存在返回 null)
    private String readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}

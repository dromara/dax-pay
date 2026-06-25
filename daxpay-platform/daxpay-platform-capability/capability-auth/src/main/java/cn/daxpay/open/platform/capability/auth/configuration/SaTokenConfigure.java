package cn.daxpay.open.platform.capability.auth.configuration;

import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.capability.auth.handler.SaRouteHandler;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

/// # Sa-Token 全局认证配置
///
/// 运行时唯一鉴权主链（固化，禁止新增并行拦截器）：
/// ```java
/// 请求 → SaInterceptor（全局拦截器，入口）
/// ↓
/// SaRouteHandler.check()（路由匹配分发）
/// ↓
/// List<RouterCheck> 按 sortNo 顺序执行
/// ↓
/// 命中任一 RouterCheck → SaRouter.stop()（放行）
/// ↓ 未命中
/// 未登录 → SecurityUtil.getUserId() 抛 NotLoginException
/// 已登录 → RouterCheckException（无权限）
/// ```
///
/// 【主链固化规则】
/// - 仅允许注册 SaInterceptor，禁止新增其他鉴权拦截器。
/// - 鉴权逻辑全部下沉到 RouterCheck SPI 实现，由 SaRouteHandler 统一编排。
/// - 拦截路径为 /**，由 platformStarterProperties.getAuth().getIgnoreUrls() 控制排除列表。
///
/// @see SaRouteHandler
/// @see cn.daxpay.open.platform.capability.auth.service.RouterCheck
@EnableConfigurationProperties(PlatformStarterProperties.class)
@RequiredArgsConstructor
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    private final PlatformStarterProperties platformStarterProperties;

    private final SaRouteHandler saRouteHandler;

    /// 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Sa-Token 综合拦截器，提供注解鉴权和路由拦截鉴权能力
        SaInterceptor saInterceptor = new SaInterceptor(handler ->
                SaRouter.match(Collections.singletonList("/**"))
                        // 不进行鉴权的地址
                        .notMatch(platformStarterProperties.getAuth().getIgnoreUrls())
                        // 注册自定义鉴权路由配置
                        .check(saRouteHandler.check(handler))
        );
        // 注册路由拦截器，自定义验证规则
        // 包装一层: SSE 等异步连接(SseEmitter)在完成/超时/异常时, Servlet 容器会做 ASYNC 重派发,
        // 此时 SaToken 上下文过滤器(OncePerRequestFilter, 仅 REQUEST 派发)不会重新执行, ThreadLocal 上下文为空,
        // 若再次进入 SaInterceptor 会抛 SaTokenContextException. 鉴权已在初始 REQUEST 派发完成, ASYNC 派发直接放行.
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                if (request.getDispatcherType() == DispatcherType.ASYNC) {
                    return true;
                }
                return saInterceptor.preHandle(request, response, handler);
            }
        }).addPathPatterns("/**");
    }
}

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
/// 运行时只有这一条鉴权链(不要再加并行鉴权拦截器):
/// ```
/// 请求 → SaInterceptor → SaRouteHandler → 各 RouterCheck(按 sortNo)
/// 任一检查通过 → SaRouter.stop() 放行
/// 全都不通过 → 未登录抛 NotLoginException / 已登录抛 RouterCheckException
/// ```
///
/// 规则:
/// - 只注册 SaInterceptor, 具体规则写在 RouterCheck 实现里, 由 SaRouteHandler 按顺序调用。
/// - 拦截路径 /**, 排除列表见 platformStarterProperties.getAuth().getIgnoreUrls()。
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
        // 包装一层: 仅在 REQUEST 派发时做鉴权.
        // SSE(SseEmitter) 在完成/超时/异常时, 容器会 ASYNC/ERROR 重派发; Sa-Token 1.45 的
        // SaTokenContextFilter 为普通 Filter Bean, 异步派发时常无 ThreadLocal 上下文,
        // 若再进 SaInterceptor → SaRouter/SaHolder 会抛 SaTokenContextException.
        // 鉴权已在首次 REQUEST 完成, 非 REQUEST 直接放行.
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                if (request.getDispatcherType() != DispatcherType.REQUEST) {
                    return true;
                }
                return saInterceptor.preHandle(request, response, handler);
            }
        }).addPathPatterns("/**");
    }
}

package cn.daxpay.open.payment.common.runtime;

import cn.daxpay.open.platform.common.mybatisplus.util.MpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/// # 支付上下文过滤器
///
/// 在 HTTP 请求开始时开启线程级身份作用域,请求结束时关闭。
/// 仅管理 [PaymentContext] 的 open/close,不负责身份填充(由切面 / 装载器完成)。
/// 请求结束时额外强制清理 `@IgnoreTenant` 引用计数，防止 depth 泄漏污染线程池。
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PaymentContextFilter extends OncePerRequestFilter implements Ordered {
    private final PaymentContext paymentContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        paymentContext.open();
        try {
            filterChain.doFilter(request, response);
        } finally {
            paymentContext.close();
            // 防 @IgnoreTenant depth 泄漏污染线程池
            int ignoreDepth = MpUtil.getIgnoreTenantDepth();
            if (ignoreDepth > 0) {
                log.error("IgnoreTenant depth leaked at request end: {}, force clearing", ignoreDepth);
            }
            MpUtil.forceClearIgnoreTenant();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}

package cn.daxpay.open.payment.common.runtime;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/// # 支付上下文过滤器
///
/// 在 HTTP 请求开始时开启线程级身份作用域,请求结束时关闭。
/// 仅管理 [PaymentContext] 的 open/close,不负责身份填充(由切面 / 装载器完成)。
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
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}

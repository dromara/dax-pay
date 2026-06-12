package org.dromara.daxpay.payment.pay.filter;

import org.dromara.daxpay.payment.common.context.PaymentScopeManager;
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

/// # 支付作用域过滤器
///
/// 在HTTP请求开始时开启作用域, 请求结束时关闭
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PaymentScopeFilter extends OncePerRequestFilter implements Ordered {
    private final PaymentScopeManager paymentScopeManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        paymentScopeManager.start();
        try {
            filterChain.doFilter(request, response);
        } finally {
            paymentScopeManager.end();
        }
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}

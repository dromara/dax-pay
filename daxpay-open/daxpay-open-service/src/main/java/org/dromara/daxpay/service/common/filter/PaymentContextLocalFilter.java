package org.dromara.daxpay.service.common.filter;

import org.dromara.daxpay.service.common.local.MchContextLocal;
import org.dromara.daxpay.service.common.local.PaymentContextLocal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 支付上下文本地过滤器
 * @author xxm
 * @since 2023/12/22
 */
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PaymentContextLocalFilter extends OncePerRequestFilter implements OrderedFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request,response);
        } finally {
            PaymentContextLocal.clear();
            MchContextLocal.clearMchNo();
        }
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE+100;
    }
}

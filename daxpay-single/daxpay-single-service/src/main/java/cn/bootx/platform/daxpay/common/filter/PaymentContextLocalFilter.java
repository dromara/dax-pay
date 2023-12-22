package cn.bootx.platform.daxpay.common.filter;

import cn.bootx.platform.daxpay.common.local.PaymentContextLocal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付上下文本地过滤器
 * @author xxm
 * @since 2023/12/22
 */
@Order(value = Integer.MIN_VALUE)
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class PaymentContextLocalFilter  extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        PaymentContextLocal.clear();
    }
}

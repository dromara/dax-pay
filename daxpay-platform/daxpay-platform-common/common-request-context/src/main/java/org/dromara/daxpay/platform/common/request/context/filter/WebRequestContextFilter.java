package org.dromara.daxpay.platform.common.request.context.filter;

import org.dromara.daxpay.platform.common.request.context.constant.RequestContextCode;
import org.dromara.daxpay.platform.common.request.context.local.RequestContextStorage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

/// # 请求上下文保存过滤器，放在过滤链最前方
///
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebRequestContextFilter extends OncePerRequestFilter implements Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            RequestContextStorage.put(RequestContextCode.METHOD, request.getMethod());
            RequestContextStorage.put(RequestContextCode.CONTEXT_PATH, request.getContextPath());
            RequestContextStorage.put(RequestContextCode.REQUEST_URI, request.getRequestURI());
            RequestContextStorage.put(RequestContextCode.REQUEST_URL, request.getRequestURL().toString());

            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
                RequestContextStorage.put(header, request.getHeader(header));
            }
            chain.doFilter(request, response);
        }
        finally {
            RequestContextStorage.clear();
        }
    }
}

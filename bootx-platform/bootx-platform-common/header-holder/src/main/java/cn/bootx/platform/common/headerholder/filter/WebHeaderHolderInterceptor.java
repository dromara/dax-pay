package cn.bootx.platform.common.headerholder.filter;

import cn.bootx.platform.common.headerholder.local.HolderContextHolder;
import cn.bootx.platform.core.code.ServletCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;

/**
 * 请求头保存
 *
 * @author xxm
 * @since 2021/4/20
 */
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class WebHeaderHolderInterceptor extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            // 保存一些Servlet请求的数据
            HolderContextHolder.put(ServletCode.METHOD, request.getMethod());
            HolderContextHolder.put(ServletCode.CONTEXT_PATH, request.getContextPath());
            HolderContextHolder.put(ServletCode.REQUEST_URI, request.getRequestURI());
            HolderContextHolder.put(ServletCode.REQUEST_URL, request.getRequestURL().toString());
            // 保存请求头数据
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
                HolderContextHolder.put(header, request.getHeader(header));
            }
            chain.doFilter(request, response);
        }
        finally {
            HolderContextHolder.clear();
        }
    }

}

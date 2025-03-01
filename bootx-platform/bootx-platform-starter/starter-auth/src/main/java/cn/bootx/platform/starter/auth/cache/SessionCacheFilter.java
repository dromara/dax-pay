package cn.bootx.platform.starter.auth.cache;

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
 * session缓存,用来减少一个请求中多次获取用户信息导致的多次访问redis
 *
 * @author xxm
 * @since 2022/1/8
 */
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SessionCacheFilter extends OncePerRequestFilter implements OrderedFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        }
        finally {
            SessionCacheLocal.clear();
        }
    }


    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE+100;
    }

}

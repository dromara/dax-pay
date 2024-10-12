package cn.bootx.platform.common.log.handler;

import cn.bootx.platform.core.code.CommonCode;
import cn.hutool.core.util.RandomUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 针对请求生成链路追踪ID
 *
 * @author xxm
 * @since 2021/4/20
 */
@Order(value = Integer.MIN_VALUE)
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class LogTraceHeaderHolderFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String traceId = RandomUtil.randomString(12);
            // 添加普通日志 TraceId
            MDC.put(CommonCode.TRACE_ID, traceId);
            chain.doFilter(request, response);
        }
        finally {
            MDC.clear();
        }
    }

}

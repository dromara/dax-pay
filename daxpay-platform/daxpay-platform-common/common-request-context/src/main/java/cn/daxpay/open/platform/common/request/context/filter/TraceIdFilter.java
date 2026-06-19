package cn.daxpay.open.platform.common.request.context.filter;

import cn.daxpay.open.platform.common.request.context.local.RequestContextStorage;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/// # 追踪ID过滤器
///
@Slf4j
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class TraceIdFilter extends OncePerRequestFilter implements Ordered {

    /// 过滤器优先级
    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 1;

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String traceId = extractOrGenerateTraceId(request);
            RequestContextStorage.put(WebHeaderCode.X_TRACE_ID, traceId);
            MDC.put(CommonCode.TRACE_ID, traceId);
            response.setHeader(WebHeaderCode.X_TRACE_ID, traceId);
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

    /// 从请求头提取或生成追踪ID
    private String extractOrGenerateTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(WebHeaderCode.X_TRACE_ID);
        if (isValidTraceId(traceId)) {
            return traceId;
        }
        return String.valueOf(IdUtil.getSnowflakeNextId());
    }

    /// 验证追踪ID的有效性
    private boolean isValidTraceId(String traceId) {
        if (StrUtil.isBlank(traceId)) {
            return false;
        }
        if (!Validator.isNumber(traceId)) {
            return false;
        }
        int length = traceId.length();
        return length >= 10 && length <= 20;
    }
}

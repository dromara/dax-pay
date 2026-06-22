package cn.daxpay.open.platform.common.request.context.filter;

import cn.daxpay.open.platform.common.request.context.local.RequestContextStorage;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.platform.core.code.WebHeaderCode;
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
/// 基于 OpenTelemetry 自动注入的 MDC traceId, 将其透传到 ThreadLocal 与响应 header。
/// 本过滤器不再自行生成 traceId, 完全依赖 OTel ServerHttpObservationFilter 的注入。
/// 执行顺序晚于 OTel Filter(HIGHEST_PRECEDENCE + 1), 以确保 MDC 中已有 traceId。
@Slf4j
@Component
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class TraceIdFilter extends OncePerRequestFilter implements Ordered {

    /// 过滤器优先级: 晚于 OTel ServerHttpObservationFilter, 确保 MDC 已注入 traceId
    public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

    @Override
    public int getOrder() {
        return ORDER;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            // 读取 OTel ServerHttpObservationFilter 已注入的 traceId
            String traceId = MDC.get(CommonCode.TRACE_ID);
            if (StrUtil.isNotBlank(traceId)) {
                // 透传到 ThreadLocal, 供业务代码通过 RequestContextHolder 访问
                RequestContextStorage.put(WebHeaderCode.X_TRACE_ID, traceId);
                // 写入响应 header, 便于前端/调用方关联
                response.setHeader(WebHeaderCode.X_TRACE_ID, traceId);
            }
            chain.doFilter(request, response);
        } finally {
            // 注意: 不要调用 MDC.clear(), OTel 自行管理 MDC 生命周期
            RequestContextStorage.clear();
        }
    }
}

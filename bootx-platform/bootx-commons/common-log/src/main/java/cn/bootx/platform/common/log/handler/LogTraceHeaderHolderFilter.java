package cn.bootx.platform.common.log.handler;

import cn.bootx.platform.common.core.code.CommonCode;
import cn.hutool.core.util.RandomUtil;
import com.plumelog.core.TraceId;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
            // 添加普通日志和 plumelog 日志的 TraceId
            MDC.put(CommonCode.TRACE_ID, traceId);
            try {
                TraceId.logTraceID.set(traceId);
            } catch (NoClassDefFoundError ignored) {}
            chain.doFilter(request, response);
        }
        finally {
            MDC.clear();
        }
    }

}

package cn.daxpay.open.payment.common.context;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/// # 通道回调身份装载过滤器
///
/// 回调路径约定 `/unipay/callback/{mchNo}/{appId}/...`。
/// 在 [PaymentContextFilter] 开启作用域后，从 path 解析 mchNo 写入 [PaymentContext]
///（回调侧身份 Bootstrap），使后续验签/查单走正常 `mch_no` 租户过滤。
/// 须晚于 [PaymentContextFilter] 执行。
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CallbackMchContextFilter extends OncePerRequestFilter implements Ordered {

    /// 匹配 .../unipay/callback/{mchNo}/...
    private static final Pattern CALLBACK_MCH_PATH =
            Pattern.compile(".*/unipay/callback/([^/]+)/.*");

    private final PaymentContext paymentContext;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (paymentContext.isOpen()) {
            String mchNo = extractMchNo(request.getRequestURI());
            if (StrUtil.isNotBlank(mchNo)) {
                paymentContext.setMchNo(mchNo);
            }
        }
        filterChain.doFilter(request, response);
    }

    /// 从回调 URI 解析商户号；非回调路径返回 null
    private static String extractMchNo(String requestUri) {
        if (StrUtil.isBlank(requestUri)) {
            return null;
        }
        Matcher matcher = CALLBACK_MCH_PATH.matcher(requestUri);
        if (!matcher.matches()) {
            return null;
        }
        return matcher.group(1);
    }

    @Override
    public int getOrder() {
        // PaymentContextFilter = HIGHEST_PRECEDENCE + 100
        return Ordered.HIGHEST_PRECEDENCE + 101;
    }
}

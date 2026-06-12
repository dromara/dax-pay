package org.dromara.daxpay.payment.merchant.filter;

import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.core.entity.UserDetail;
import org.dromara.daxpay.platform.iam.service.client.ClientCodeService;
import org.dromara.daxpay.platform.capability.auth.util.SecurityUtil;
import org.dromara.daxpay.payment.common.context.PaymentContext;
import org.dromara.daxpay.payment.merchant.service.query.MerchantQueryFacadeService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/// # 商户信息过滤器
///
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MchContextLocalFilter extends OncePerRequestFilter implements Ordered {
    private final MerchantQueryFacadeService merchantQueryFacadeService;
    private final ClientCodeService clientCodeService;
    private final PaymentContext apiContext;

    /// 需要晚于 {@link org.springframework.web.filter.RequestContextFilter} 执行, 否则获取不到登录用户
    /// RequestContextFilter 默认加载优先级 为 - 150
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 只处理商户端
        String clientCode = clientCodeService.getClientCode();
        if (ClientEnum.MERCHANT.getCode().equals(clientCode)) {
            // 是否登录
            Optional<UserDetail> currentUser = SecurityUtil.getCurrentUser();
            currentUser.ifPresent(userDetail -> {
                // 登录后获取关联商户号
                String mchNo = merchantQueryFacadeService.findMchNoByUserId(userDetail.getId());
                apiContext.getTradeInfo().setMchNo(mchNo);
            });
        }
        filterChain.doFilter(request, response);
    }
}


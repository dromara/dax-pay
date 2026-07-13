package cn.daxpay.open.payment.merchant.filter;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
import cn.daxpay.open.payment.common.runtime.PaymentContext;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserService;
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
    private final MerchantUserService merchantUserService;
    private final ClientCodeService clientCodeService;
    private final PaymentContext paymentContext;

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
                String mchNo = merchantUserService.findMchNoByUserId(userDetail.getId());
                paymentContext.setMchNo(mchNo);
            });
        }
        filterChain.doFilter(request, response);
    }
}


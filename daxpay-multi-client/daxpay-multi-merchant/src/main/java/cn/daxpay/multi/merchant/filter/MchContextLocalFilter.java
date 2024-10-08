package cn.daxpay.multi.merchant.filter;

import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.daxpay.multi.merchant.service.merchant.MerchantUserService;
import cn.daxpay.multi.service.common.local.MchContextLocal;
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
import java.util.Optional;

/**
 * 商户信息过滤器
 * @author xxm
 * @since 2024/7/17
 */
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MchContextLocalFilter extends OncePerRequestFilter implements OrderedFilter {
    private final MerchantUserService merchantUserService;

    /**
     * 需要晚于 {@link org.springframework.web.filter.RequestContextFilter} 执行, 否则获取不到登录用户
     * RequestContextFilter 默认加载优先级 为 - 150
     */
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 是否登录
            Optional<UserDetail> currentUser = SecurityUtil.getCurrentUser();
            currentUser.ifPresent(userDetail -> {
                // 登录后获取关联商户号
                String mchNo = merchantUserService.findByUserId(userDetail.getId());
                MchContextLocal.setMchNo(mchNo);
            });

        } finally {
            filterChain.doFilter(request,response);
        }
    }
}

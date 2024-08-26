package cn.daxpay.multi.merchant.filter;

import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.daxpay.multi.service.common.local.MchContextLocal;
import cn.daxpay.multi.merchant.service.merchant.MerchantUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * 商户信息过滤器
 * @author xxm
 * @since 2024/7/17
 */
@Order(value = Integer.MIN_VALUE+1000)
@Component
@RequiredArgsConstructor
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class MchContextLocalFilter extends OncePerRequestFilter {
    private final MerchantUserService merchantUserService;

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

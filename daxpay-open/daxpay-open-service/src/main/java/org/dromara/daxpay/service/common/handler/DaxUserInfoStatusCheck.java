package org.dromara.daxpay.service.common.handler;

import cn.bootx.platform.iam.service.client.ClientCodeService;
import cn.bootx.platform.starter.auth.authentication.UserInfoStatusCheck;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import org.dromara.daxpay.service.common.code.DaxPayCode;
import org.dromara.daxpay.service.merchant.service.info.MerchantUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/**
 * DaxPay登录验证处理，没有对应终端的权限不运行登录
 * @author xxm
 * @since 2025/8/10
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DaxUserInfoStatusCheck implements UserInfoStatusCheck {
    private final MerchantUserService merchantUserService;
    private final ClientCodeService clientCodeService;

    /**
     * 检查用户是否拥有当前终端的权限
     *
     * @param authInfoResult 认证返回结果
     * @param context        登录认证上下文
     */
    @Override
    public void check(AuthInfoResult authInfoResult, LoginAuthContext context) {
        // 判断终端
        String clientCode = clientCodeService.getClientCode();
        // 商户端
        if (Objects.equals(clientCode, DaxPayCode.Client.MERCHANT)){
            Optional.ofNullable(merchantUserService.findByUserId(authInfoResult.getUserDetail().getId()))
                    .orElseThrow(() -> new LoginFailureException("您没有商户端的登录权限"));
        } else {
            // 运营端
            String merchant = merchantUserService.findByUserId(authInfoResult.getUserDetail().getId());
            if (merchant != null){
                throw new LoginFailureException("您没有运营端的权限，请使用商户端登录");
            }
        }
    }
}

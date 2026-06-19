package cn.daxpay.open.payment.common.handler;

import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.capability.auth.authentication.UserInfoStatusCheck;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.payment.common.service.MerchantPaymentQueryService;
import cn.daxpay.open.payment.common.service.MerchantUserQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/// # DaxPay登录验证处理（开源版）
///
@Slf4j
@Component
@RequiredArgsConstructor
public class DaxUserInfoStatusCheck implements UserInfoStatusCheck {
    private final MerchantUserQueryService merchantUserQueryService;
    private final MerchantPaymentQueryService merchantPaymentQueryService;
    private final ClientCodeService clientCodeService;

    /// 检查用户是否拥有当前终端的权限
    ///
    /// @param authInfoResult 认证返回结果
    /// @param context        登录认证上下文
    @Override
    public void check(AuthInfoResult authInfoResult, LoginAuthContext context) {
        // 判断终端
        Long userId = authInfoResult.getUserDetail().getId();
        // 商户端
        if (Objects.equals(clientCodeService.getClientCode(), cn.daxpay.open.platform.core.enums.client.ClientEnum.MERCHANT.getCode())) {
            String merchantNo = Optional.ofNullable(merchantUserQueryService.findMchNoByUserId(userId))
                    // 登录: 您没有商户端的登录权限
                    .orElseThrow(() -> new LoginFailureException(CommonCode.FAIL_CODE, "error.payment.login.noMerchantPerm"));
            var merchant = Optional.ofNullable(merchantPaymentQueryService.getMerchantByMchNo(merchantNo))
                    // 登录: 您没有商户端的登录权限
                    .orElseThrow(() -> new LoginFailureException(CommonCode.FAIL_CODE, "error.payment.login.noMerchantPerm"));
            if (Objects.equals(merchant.getStatus(), MerchantStatusEnum.DISABLED.getCode())) {
                // 登录: 该商户已禁用
            throw new LoginFailureException(CommonCode.FAIL_CODE, "error.payment.login.mchDisabled");
            }
        } else {
            // 运营端
            String merchant = merchantUserQueryService.findMchNoByUserId(userId);
            if (merchant != null) {
                // 登录: 您没有运营端的权限，请使用商户端登录
            throw new LoginFailureException(CommonCode.FAIL_CODE, "error.payment.login.noAdminPermUseMerchant");
            }
        }
    }
}

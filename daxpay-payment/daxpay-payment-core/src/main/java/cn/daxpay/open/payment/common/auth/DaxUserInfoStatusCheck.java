package cn.daxpay.open.payment.common.auth;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.core.enums.merchant.MerchantStatusEnum;
import cn.daxpay.open.platform.iam.service.client.ClientCodeService;
import cn.daxpay.open.platform.capability.auth.authentication.UserInfoStatusCheck;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import cn.daxpay.open.platform.core.code.CommonCode;
import cn.daxpay.open.payment.common.access.MerchantAccessPort;
import cn.daxpay.open.payment.merchant.service.user.MerchantUserService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

/// # DaxPay 登录身份域检查（开源版）
///
/// **仅服务 IAM 登录链路**（密码 / 社交等），与 TenantLine / 网关 mch_no 隔离无关。
/// 优先使用登录上下文中的 clientCode，避免仅依赖请求头导致旁路漏检。
@Slf4j
@Component
@RequiredArgsConstructor
public class DaxUserInfoStatusCheck implements UserInfoStatusCheck {
    private final MerchantUserService merchantUserService;
    private final MerchantAccessPort merchantAccessPort;
    private final ClientCodeService clientCodeService;

    /// 检查用户是否拥有当前终端的权限
    ///
    /// @param authInfoResult 认证返回结果
    /// @param context        登录认证上下文
    @Override
    public void check(AuthInfoResult authInfoResult, LoginAuthContext context) {
        Long userId = authInfoResult.getUserDetail().getId();
        String clientCode = resolveClientCode(context);
        // 商户端
        if (Objects.equals(clientCode, ClientEnum.MERCHANT.getCode())) {
            String merchantNo = Optional.ofNullable(merchantUserService.findMchNoByUserId(userId))
                    // 登录: 您没有商户端的登录权限
                    .orElseThrow(() -> new LoginFailureException(CommonCode.FAIL_CODE, "error.payment.login.noMerchantPerm"));
            var merchant = Optional.ofNullable(merchantAccessPort.getMerchantByMchNo(merchantNo))
                    // 登录: 您没有商户端的登录权限
                    .orElseThrow(() -> new LoginFailureException(CommonCode.FAIL_CODE, "error.payment.login.noMerchantPerm"));
            if (Objects.equals(merchant.getStatus(), MerchantStatusEnum.DISABLED.getCode())) {
                // 登录: 该商户已禁用
                throw new LoginFailureException(CommonCode.FAIL_CODE, "error.payment.login.mchDisabled");
            }
            return;
        }
        // 运营端(及其他非商户端): 商户用户不得登录运营身份域
        if (Objects.equals(clientCode, ClientEnum.ADMIN.getCode())) {
            String merchant = merchantUserService.findMchNoByUserId(userId);
            if (merchant != null) {
                // 登录: 您没有运营端的权限，请使用商户端登录
                throw new LoginFailureException(CommonCode.FAIL_CODE, "error.payment.login.noAdminPermUseMerchant");
            }
        }
    }

    /// 解析当前登录终端: 优先上下文, 其次请求头
    private String resolveClientCode(LoginAuthContext context) {
        if (context != null && StrUtil.isNotBlank(context.getClientCode())) {
            return context.getClientCode();
        }
        return clientCodeService.getClientCode();
    }
}

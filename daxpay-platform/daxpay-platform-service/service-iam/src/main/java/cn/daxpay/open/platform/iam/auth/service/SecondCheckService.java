package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.iam.param.auth.LoginContentParam;
import cn.daxpay.open.platform.iam.result.auth.SecondCheckResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/// # 二次校验信息服务
///
/// 供登录页预知当前是否启用了双因素认证(平台级开关), 前端据此决定是否展示相关提示。
/// 具体某用户是否需要二次验证, 在密码校验通过后由 [cn.daxpay.open.platform.iam.endpoint.TokenService] 判定。
///
@Service
@RequiredArgsConstructor
public class SecondCheckService {

    private final IamSecurityConfigService iamSecurityConfigService;

    /// 获取当前登录链路需要的二次校验信息
    public SecondCheckResult getSecondCheck(LoginContentParam param) {
        boolean platformEnabled = Boolean.TRUE.equals(
                iamSecurityConfigService.getTwoFactorAuthConfig().getEnabled());
        return new SecondCheckResult()
                .setRequired(platformEnabled)
                .setType(platformEnabled ? "TOTP" : "NONE");
    }

}

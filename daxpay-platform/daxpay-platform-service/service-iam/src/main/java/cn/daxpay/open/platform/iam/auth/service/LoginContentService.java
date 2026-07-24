package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.capability.auth.authentication.Authenticator;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.exception.auth.ApplicationNotFoundException;
import cn.daxpay.open.platform.iam.param.auth.LoginContentParam;
import cn.daxpay.open.platform.iam.result.auth.LoginContentResult;
import cn.daxpay.open.platform.iam.service.social.SocialAutoLoginConfigService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformSocialAutoLoginConfig;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 登录上下文服务
///
@Service
@RequiredArgsConstructor
public class LoginContentService {

    private final List<Authenticator> authenticators;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final SocialAutoLoginConfigService socialAutoLoginConfigService;

    /// 获取登录页上下文信息
    public LoginContentResult getLoginContent(LoginContentParam param) {
        String clientCode = param != null ? param.getClientId() : null;
        if (StrUtil.isNotBlank(clientCode)) {
            ClientEnum.findByCode(clientCode)
                    .orElseThrow(ApplicationNotFoundException::new);
        }
        PlatformLoginSecurityConfig loginSecurity = iamSecurityConfigService.getLoginSecurity();
        LoginContentResult result = new LoginContentResult()
                .setLoginTypes(authenticators.stream()
                        .map(Authenticator::getLoginType)
                        .distinct()
                        .toList())
                // 是否启用验证码触发（登录失败达阈值后要求输入验证码）
                .setEnableCaptcha(Boolean.TRUE.equals(loginSecurity.getCaptchaEnabled()))
                .setPasswordEncrypted(false);
        // 按本端下发自动登录片段(无 client 时不下发, 保持关闭)
        if (StrUtil.isNotBlank(clientCode)) {
            PlatformSocialAutoLoginConfig.ClientAutoLogin auto =
                    socialAutoLoginConfigService.resolveForClient(clientCode);
            result.setAutoSocialLogin(new LoginContentResult.AutoSocialLogin()
                    .setEnabled(Boolean.TRUE.equals(auto.getEnabled()))
                    .setSources(auto.resolveSources()));
        } else {
            result.setAutoSocialLogin(new LoginContentResult.AutoSocialLogin().setEnabled(false));
        }
        return result;
    }

}

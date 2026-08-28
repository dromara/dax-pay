package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.capability.auth.authentication.Authenticator;
import cn.daxpay.open.platform.capability.auth.code.AuthLoginTypeCode;
import cn.daxpay.open.platform.capability.social.justauth.SocialSourceEnum;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.exception.auth.ApplicationNotFoundException;
import cn.daxpay.open.platform.iam.param.auth.LoginContentParam;
import cn.daxpay.open.platform.iam.result.auth.LoginContentResult;
import cn.daxpay.open.platform.iam.service.social.SocialAutoLoginConfigService;
import cn.daxpay.open.platform.iam.service.social.SocialLoginConfigService;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformLoginSecurityConfig;
import cn.daxpay.open.platform.system.entity.config.platform.security.PlatformSocialAutoLoginConfig;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/// # 登录上下文服务
///
@Service
@RequiredArgsConstructor
public class LoginContentService {

    private final List<Authenticator> authenticators;

    private final IamSecurityConfigService iamSecurityConfigService;

    private final SocialAutoLoginConfigService socialAutoLoginConfigService;

    private final SocialLoginConfigService socialLoginConfigService;

    /// 获取登录页上下文信息
    public LoginContentResult getLoginContent(LoginContentParam param) {
        String clientCode = param != null ? param.getClientId() : null;
        if (StrUtil.isNotBlank(clientCode)) {
            ClientEnum.findByCode(clientCode)
                    .orElseThrow(ApplicationNotFoundException::new);
        }
        PlatformLoginSecurityConfig loginSecurity = iamSecurityConfigService.getLoginSecurity();
        // 登录方式聚合: 账密等走 Authenticator SPI 自动聚合; 通行密钥为两阶段交互不走 SPI, 按平台开关手动追加
        List<String> loginTypes = new ArrayList<>(authenticators.stream()
                .map(Authenticator::getLoginType)
                .distinct()
                .toList());
        if (Boolean.TRUE.equals(iamSecurityConfigService.getWebAuthnConfig().getEnabled())) {
            loginTypes.add(AuthLoginTypeCode.PASSKEY);
        }
        LoginContentResult result = new LoginContentResult()
                .setLoginTypes(loginTypes)
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
        }
        else {
            result.setAutoSocialLogin(new LoginContentResult.AutoSocialLogin().setEnabled(false));
        }
        // 小程序快捷登录可用平台(applet 型 source 中已配置且已启用的, 与 client 无关全局判定)
        List<String> appletSources = Arrays.stream(SocialSourceEnum.values())
                .filter(SocialSourceEnum::isApplet)
                .map(SocialSourceEnum::getCode)
                .filter(code -> socialLoginConfigService.findEnabledBySource(code) != null)
                .toList();
        result.setAppletSources(appletSources);
        return result;
    }

}

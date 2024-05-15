package cn.bootx.platform.iam.core.auth.login;

import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.starter.auth.authentication.AbstractAuthentication;
import cn.bootx.platform.starter.auth.code.AuthLoginTypeCode;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.baseapi.core.captcha.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 手机号登录
 *
 * @author xxm
 * @since 2021/8/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PhoneLoginHandler implements AbstractAuthentication {

    // 手机号
    private final String phoneParameter = "phone";

    // 短信验证码
    private final String captchaParameter = "smsCaptcha";

    // 手机验证码类型
    private final String smsCaptchaType = "login";

    private final UserInfoManager userInfoManager;

    private final CaptchaService captchaService;

    @Override
    public String getLoginType() {
        return AuthLoginTypeCode.PHONE;
    }

    /**
     * 认证
     */
    @Override
    public AuthInfoResult attemptAuthentication(LoginAuthContext context) {
        HttpServletRequest request = context.getRequest();
        String phone = request.getParameter(phoneParameter);
        String captcha = request.getParameter(captchaParameter);

        // 比较验证码是否正确
        if (!captchaService.validateSmsCaptcha(phone, captcha, smsCaptchaType)) {
            throw new LoginFailureException(phone, "短信验证码不正确");
        }
        // 获取用户信息
        UserInfo userInfo = userInfoManager.findByPhone(phone)
            .orElseThrow(() -> new LoginFailureException(phone, "手机号不存在"));

        captchaService.deleteSmsCaptcha(phone, smsCaptchaType);
        return new AuthInfoResult().setUserDetail(userInfo.toUserDetail()).setId(userInfo.getId());
    }

}

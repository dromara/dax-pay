package cn.bootx.platform.iam.core.auth.login;

import cn.bootx.platform.baseapi.core.captcha.service.CaptchaService;
import cn.bootx.platform.common.core.entity.UserDetail;
import cn.bootx.platform.common.core.exception.BizException;
import cn.bootx.platform.common.core.util.RegexUtil;
import cn.bootx.platform.iam.core.security.password.service.PasswordLoginFailRecordService;
import cn.bootx.platform.iam.core.user.service.UserQueryService;
import cn.bootx.platform.iam.dto.user.UserInfoDto;
import cn.bootx.platform.starter.auth.authentication.UsernamePasswordAuthentication;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.AuthLoginType;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.starter.auth.exception.UserNotFoundException;
import cn.bootx.platform.starter.auth.util.PasswordEncoder;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 账号密码登陆方式实现
 *
 * @author xxm
 * @since 2021/8/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
@SuppressWarnings("FieldCanBeLocal")
public class PasswordLoginHandler implements UsernamePasswordAuthentication {

    private final String USERNAME_PARAMETER = "account";

    private final String PASSWORD_PARAMETER = "password";

    // 前端传入的验证码
    private final String CAPTCHA_PARAMETER = "captcha";

    // 前端传入的验证码的key
    private final String CAPTCHA_KEY_PARAMETER = "captchaKey";

    @Resource
    @Getter
    private PasswordLoginFailRecordService passwordLoginFailRecordService;

    @Resource
    @Getter
    private PasswordEncoder passwordEncoder;

    @Resource
    @Getter
    private UserQueryService userQueryService;

    @Resource
    @Getter
    private CaptchaService captchaService;

    /**
     * 认证前置操作, 默认处理验证码
     */
    @Override
    public void authenticationBefore(LoginAuthContext context) {
        AuthLoginType authLoginType = context.getAuthLoginType();
        HttpServletRequest request = context.getRequest();
        // 开启验证码验证
        if (authLoginType.isCaptcha()) {
            String captcha = this.obtainCaptcha(request);
            String captchaKey = this.obtainCaptchaKey(request);
            if (StrUtil.isBlank(captcha)) {
                throw new BizException("验证码为空");
            }
            if (!captchaService.validateImgCaptcha(captchaKey, captcha)) {
                String username = this.obtainUsername(request);
                throw new LoginFailureException(username, "验证码不正确");
            }
        }
    }

    /**
     * 认证
     */
    @Override
    public @NotNull AuthInfoResult attemptAuthentication(LoginAuthContext context) {
        String username = this.obtainUsername(context.getRequest());
        String password = this.obtainPassword(context.getRequest());
        UserDetail userDetail = this.loadUserByUsername(username);
        String saltPassword = passwordEncoder.encode(password);
        // 比对密码未通过
        if (!Objects.equals(saltPassword, userDetail.getPassword())) {
            // 非系统管理员进行错误处理, 包括记录错误次数和账号锁定等操作
            if (!userDetail.isAdmin()){
                String errMsg = passwordLoginFailRecordService.passwordError(userDetail.getId());
                throw new LoginFailureException(userDetail.getUsername(), errMsg);
            }
            throw new LoginFailureException(userDetail.getUsername(), "账号或密码不正确");
        }
        return new AuthInfoResult().setId(userDetail.getId()).setUserDetail(userDetail);
    }

    /**
     * 认证后操作
     */
    @Override
    public void authenticationAfter(AuthInfoResult authInfoResult, LoginAuthContext context) {
        UserDetail userDetail = authInfoResult.getUserDetail();
        // 非管理员登录成功后清除错误次数等信息
        if (!userDetail.isAdmin()){
            passwordLoginFailRecordService.clearFailCount(userDetail.getId());
        }
        // 清除验证码
        String captchaKey = this.obtainCaptchaKey(context.getRequest());
        captchaService.deleteImgCaptcha(captchaKey);
    }

    /**
     * 根据账号获取密码
     */
    public UserDetail loadUserByUsername(String username) throws UserNotFoundException {
        UserInfoDto userInfoDto;
        // 1. 获取账号密码
        if (RegexUtil.isEmailPattern(username)) {
            // 根据 Email 获取用户信息
            userInfoDto = userQueryService.findByEmail(username);
        }
        else if (RegexUtil.isPhonePattern(username)) {
            // 根据 手机号 获取用户信息
            userInfoDto = userQueryService.findByPhone(username);
        }
        else {
            // 根据 账号 获取账户信息
            userInfoDto = userQueryService.findByAccount(username);
        }
        if (Objects.isNull(userInfoDto)) {
            throw new UserNotFoundException(username);
        }
        return userInfoDto.toUserDetail();
    }


    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.PASSWORD_PARAMETER);
    }

    @Nullable
    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.USERNAME_PARAMETER);
    }

    @Nullable
    protected String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter(this.CAPTCHA_PARAMETER);
    }

    @Nullable
    protected String obtainCaptchaKey(HttpServletRequest request) {
        return request.getParameter(this.CAPTCHA_KEY_PARAMETER);
    }

}

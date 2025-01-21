package cn.bootx.platform.iam.auth.login;

import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.iam.result.user.UserInfoResult;
import cn.bootx.platform.iam.service.service.UserQueryService;
import cn.bootx.platform.starter.auth.authentication.AbstractAuthentication;
import cn.bootx.platform.starter.auth.code.AuthLoginTypeCode;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.starter.auth.exception.UserNotFoundException;
import cn.hutool.crypto.digest.BCrypt;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

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
public class PasswordLoginHandler implements AbstractAuthentication {

    private final String ACCOUNT_PARAMETER = "account";

    private final String PASSWORD_PARAMETER = "password";

    @Resource
    @Getter
    private UserQueryService userQueryService;

    /**
     * 获取终端编码
     */
    @Override
    public String getLoginType() {
        return AuthLoginTypeCode.PASSWORD;
    }

    /**
     * 认证
     */
    @Override
    public @NotNull AuthInfoResult attemptAuthentication(LoginAuthContext context) {
        String account = this.obtainAccount(context.getRequest());
        String password = this.obtainPassword(context.getRequest());
        UserDetail userDetail = this.loadUserByAccount(account);
        // 比对密码未通过
        if (!BCrypt.checkpw(password, userDetail.getPassword())) {
            // 非系统管理员进行错误处理, 包括记录错误次数和账号锁定等操作
            throw new LoginFailureException(userDetail.getAccount(), "账号或密码不正确");
        }
        return new AuthInfoResult().setId(userDetail.getId()).setUserDetail(userDetail);
    }

    /**
     * 根据账号获取密码
     */
    public UserDetail loadUserByAccount(String account) throws UserNotFoundException {
        // 根据 账号 获取账户信息
        UserInfoResult userInfoResult = userQueryService.findByAccount(account);
        if (Objects.isNull(userInfoResult)) {
            throw new UserNotFoundException(account);
        }
        return userInfoResult.toUserDetail();
    }


    @Nullable
    protected String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.PASSWORD_PARAMETER);
    }

    @Nullable
    protected String obtainAccount(HttpServletRequest request) {
        return request.getParameter(this.ACCOUNT_PARAMETER);
    }
}

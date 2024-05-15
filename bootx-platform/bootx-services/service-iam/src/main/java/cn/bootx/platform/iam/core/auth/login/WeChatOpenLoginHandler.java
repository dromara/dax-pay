package cn.bootx.platform.iam.core.auth.login;

import cn.bootx.platform.iam.core.third.dao.UserThirdManager;
import cn.bootx.platform.iam.core.third.entity.UserThird;
import cn.bootx.platform.iam.core.third.service.UserTiredOperateService;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.starter.auth.authentication.OpenIdAuthentication;
import cn.bootx.platform.starter.auth.code.AuthLoginTypeCode;
import cn.bootx.platform.starter.auth.configuration.AuthProperties;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 微信开放平台登录
 *
 * @author xxm
 * @since 2022/8/4
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatOpenLoginHandler implements OpenIdAuthentication {

    private final UserTiredOperateService userTiredOperateService;

    private final UserThirdManager userThirdManager;

    private final UserInfoManager userInfoManager;

    private final AuthProperties authProperties;

    @Override
    public String getLoginType() {
        return AuthLoginTypeCode.WE_CHAT_OPEN;
    }

    /**
     * 认证
     */
    @Override
    public AuthInfoResult attemptAuthentication(LoginAuthContext context) {
        String authCode = context.getRequest().getParameter(AuthLoginTypeCode.AUTH_CODE);
        String state = context.getRequest().getParameter(AuthLoginTypeCode.STATE);

        AuthUser authUser = this.getAuthUser(authCode, state);

        // 获取企微关联的用户id
        UserThird userThird = userThirdManager.findByField(UserThird::getWeChatId, authUser.getUuid())
            .orElseThrow(() -> new LoginFailureException("微信没有找到绑定的用户"));

        // 获取用户信息
        UserInfo userInfo = userInfoManager.findById(userThird.getUserId())
            .orElseThrow(() -> new LoginFailureException("用户不存在"));

        return new AuthInfoResult().setUserDetail(userInfo.toUserDetail()).setId(userInfo.getId());
    }

    /**
     * 获取登录地址
     */
    @Override
    public String getLoginUrl() {
        AuthRequest authRequest = this.getAuthRequest();
        return authRequest.authorize(AuthStateUtils.createState());
    }

    /**
     * 获取关联的的第三方平台用户信息
     */
    @Override
    @SuppressWarnings("unchecked")
    public AuthUser getAuthUser(String authCode, String state) {
        AuthRequest authRequest = this.getAuthRequest();
        AuthCallback callback = AuthCallback.builder().code(authCode).state(state).build();
        AuthResponse<AuthUser> response = authRequest.login(callback);
        if (!Objects.equals(response.getCode(), AuthLoginTypeCode.SUCCESS)) {
            log.error("微信登录报错: {}", response.getMsg());
            throw new LoginFailureException("微信登录出错");
        }
        return response.getData();
    }

    /**
     * 绑定用户
     */
    @Override
    public void bindUser(String authCode, String state) {
        Long userId = SecurityUtil.getUserId();
        AuthUser authUser = this.getAuthUser(authCode, state);
        userTiredOperateService.checkOpenIdBind(authUser.getUuid(), UserThird::getWeChatOpenId);
        userTiredOperateService.bindOpenId(userId, authUser.getUuid(), UserThird::setWeChatOpenId);
        userTiredOperateService.bindOpenInfo(userId, authUser, AuthLoginTypeCode.WE_CHAT);
    }

    /**
     * 获取微信认证请求
     */
    private AuthWeChatOpenRequest getAuthRequest() {
        val thirdLogin = authProperties.getThirdLogin().getWeChatOpen();
        if (Objects.isNull(thirdLogin)) {
            throw new LoginFailureException("微信开放登录配置有误");
        }
        return new AuthWeChatOpenRequest(AuthConfig.builder()
            .clientId(thirdLogin.getClientId())
            .clientSecret(thirdLogin.getClientSecret())
            .redirectUri(thirdLogin.getRedirectUri())
            .build());
    }

}

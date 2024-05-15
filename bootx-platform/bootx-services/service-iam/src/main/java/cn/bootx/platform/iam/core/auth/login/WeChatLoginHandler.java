package cn.bootx.platform.iam.core.auth.login;

import cn.bootx.platform.iam.core.third.dao.UserThirdManager;
import cn.bootx.platform.iam.core.third.entity.UserThird;
import cn.bootx.platform.iam.core.third.service.UserTiredOperateService;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.starter.auth.authentication.OpenIdAuthentication;
import cn.bootx.platform.starter.auth.code.AuthLoginTypeCode;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.starter.wechat.core.login.service.WeChatQrLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Component;

/**
 * 微信登录(公众号)
 *
 * @author xxm
 * @since 2021/8/2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatLoginHandler implements OpenIdAuthentication {

    private final UserTiredOperateService userTiredOperateService;

    private final WeChatQrLoginService weChatQrLoginService;

    private final UserThirdManager userThirdManager;

    private final UserInfoManager userInfoManager;

    @Override
    public String getLoginType() {
        return AuthLoginTypeCode.WE_CHAT;
    }

    /**
     * 认证
     */
    @Override
    public AuthInfoResult attemptAuthentication(LoginAuthContext context) {
        String authCode = context.getRequest().getParameter(AuthLoginTypeCode.AUTH_CODE);
        AuthUser authUser = this.getAuthUser(authCode, null);

        // 获取微信公众号关联的用户id
        UserThird userThird = userThirdManager.findByField(UserThird::getWeChatId, authUser.getUuid())
            .orElseThrow(() -> new LoginFailureException("没有找到绑定的微信公众号用户"));

        // 获取用户信息
        UserInfo userInfo = userInfoManager.findById(userThird.getUserId())
            .orElseThrow(() -> new LoginFailureException("用户不存在"));

        return new AuthInfoResult().setUserDetail(userInfo.toUserDetail()).setId(userInfo.getId());
    }

    /**
     * 获取关联的的第三方平台用户信息
     * @param authCode key 值, 可以用来拿用户信息
     */
    @Override
    public AuthUser getAuthUser(String authCode, String state) {
        // 根据微信二维码的值获取关联扫码的微信信息
        String uuid = weChatQrLoginService.getOpenId(authCode);
        AuthUser authUser = new AuthUser();
        authUser.setNickname("未知");
        authUser.setUsername("未知");
        authUser.setAvatar("未知");
        authUser.setUuid(uuid);
        weChatQrLoginService.clear(authCode);
        return authUser;
    }

    /**
     * 绑定用户
     */
    @Override
    public void bindUser(String authCode, String state) {
        Long userId = SecurityUtil.getUserId();
        AuthUser authUser = this.getAuthUser(authCode, state);
        userTiredOperateService.checkOpenIdBind(authUser.getUuid(), UserThird::getWeChatId);
        userTiredOperateService.bindOpenId(userId, authUser.getUuid(), UserThird::setWeChatId);
        userTiredOperateService.bindOpenInfo(userId, authUser, AuthLoginTypeCode.WE_CHAT);
    }

}

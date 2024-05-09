package cn.bootx.platform.iam.core.auth.login;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.bootx.platform.common.jackson.util.JacksonUtil;
import cn.bootx.platform.iam.core.third.dao.UserThirdManager;
import cn.bootx.platform.iam.core.third.entity.UserThird;
import cn.bootx.platform.iam.core.third.entity.UserThirdInfo;
import cn.bootx.platform.iam.core.third.service.UserTiredOperateService;
import cn.bootx.platform.iam.core.user.dao.UserInfoManager;
import cn.bootx.platform.iam.core.user.entity.UserInfo;
import cn.bootx.platform.starter.auth.authentication.OpenIdAuthentication;
import cn.bootx.platform.starter.auth.code.AuthLoginTypeCode;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import cn.bootx.platform.starter.wechat.core.user.service.WeChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeChatAppletLoginHandler implements OpenIdAuthentication {


    private final UserTiredOperateService userTiredOperateService;
    private final WxMaService wxMaService;

    private final UserThirdManager userThirdManager;

    private final UserInfoManager userInfoManager;


    private final WeChatUserService weChatUserService;

    @Override
    public String getLoginType() {
        return AuthLoginTypeCode.WE_CHAT_APPLET;
    }

    @Override
    public AuthInfoResult attemptAuthentication(LoginAuthContext context) {

        String authCode = context.getRequest().getParameter(AuthLoginTypeCode.WE_CHAT_APPLET);

        AuthUser authUser = this.getAuthUser(authCode, null);

        // 获取企微关联的用户id
        UserThird userThird = userThirdManager.findByField(UserThird::getWeChatId, authUser.getUuid())
                .orElseThrow(() -> new LoginFailureException("微信没有找到绑定的用户"));

        // 获取用户信息
        UserInfo userInfo = userInfoManager.findById(userThird.getUserId())
                .orElseThrow(() -> new LoginFailureException("用户不存在"));

        return new AuthInfoResult().setUserDetail(userInfo.toUserDetail()).setId(userInfo.getId());
    }

    @Override
    public AuthUser getAuthUser(String authCode, String state) {
        try {
            WxMaJscode2SessionResult result = wxMaService.getUserService().getSessionInfo(authCode);
            log.debug("微信服务器返回的用户信息:{}", JacksonUtil.toJson(result));
            return AuthUser.builder()
                    .avatar("")
                    .uuid(result.getOpenid()).
                    nickname("未知")
                    .username("未知")
                    .avatar("未知")
                    .build();
        } catch (WxErrorException e) {
            log.error("请求微信服务器异常:{}", e.getError());
            throw new RuntimeException(e);
        }

    }

    @Override
    public void bindUser(String authCode, String state) {
        Long userId = SecurityUtil.getUserId();
        AuthUser authUser = this.getAuthUser(authCode, state);
        userTiredOperateService.checkOpenIdBind(authUser.getUuid(), UserThird::getWeChatId);
        userTiredOperateService.bindOpenId(userId, authUser.getUuid(), UserThird::setWeChatId);

        UserThirdInfo userThirdInfo = new UserThirdInfo().setUserId(userId)
                .setClientCode(AuthLoginTypeCode.WE_CHAT)
                .setUsername(authUser.getUsername())
                .setNickname(authUser.getNickname())
                .setAvatar(authUser.getAvatar())
                .setThirdUserId(authUser.getUuid());
        userTiredOperateService.bindOpenInfo(userThirdInfo);
    }
}

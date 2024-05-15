package cn.bootx.platform.iam.core.auth.service;

import cn.bootx.platform.starter.auth.authentication.OpenIdAuthentication;
import cn.bootx.platform.starter.auth.entity.ThirdAuthCode;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 三方登录
 *
 * @author xxm
 * @since 2022/6/29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ThirdLoginService {

    private final List<OpenIdAuthentication> openIdAuthentications;

    /**
     * 获取登录地址
     */
    public String getLoginUrl(String loginType) {
        OpenIdAuthentication openIdAuthentication = this.getOpenIdAuthentication(loginType);
        return openIdAuthentication.getLoginUrl();
    }

    /**
     * 回调获取认证码
     */
    public ThirdAuthCode getAuthCode(String loginType, AuthCallback authCallback) {
        OpenIdAuthentication openIdAuthentication = this.getOpenIdAuthentication(loginType);
        return openIdAuthentication.getAuthCode(authCallback);
    }

    /**
     * 获取认证器
     */
    private OpenIdAuthentication getOpenIdAuthentication(String loginType) {
        return openIdAuthentications.stream()
            .filter(o -> o.adaptation(loginType))
            .findFirst()
            .orElseThrow(() -> new LoginFailureException("未找到对应的终端认证器"));
    }

}

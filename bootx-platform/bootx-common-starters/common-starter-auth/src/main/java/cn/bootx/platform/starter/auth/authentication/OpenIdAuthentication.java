package cn.bootx.platform.starter.auth.authentication;

import cn.bootx.platform.starter.auth.entity.ThirdAuthCode;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;

/**
 * OpenId登录认证器 (微信/钉钉/飞书/QQ/企微等)
 *
 * @author xxm
 * @since 2021/7/30
 */
public interface OpenIdAuthentication extends AbstractAuthentication {

    /**
     * 获取登录地址
     */
    default String getLoginUrl() {
        throw new UnsupportedOperationException();
    }

    /**
     * 获取认证授权码
     */
    default ThirdAuthCode getAuthCode(AuthCallback callback) {
        return new ThirdAuthCode().setCode(callback.getCode()).setState(callback.getState());
    }

    /**
     * 获取关联的的第三方平台用户信息
     */
    default AuthUser getAuthUser(String authCode, String state) {
        return null;
    }

    /**
     * 绑定用户
     */
    default void bindUser(String authCode, String state) {
        throw new UnsupportedOperationException();
    }

}

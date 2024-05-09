package cn.bootx.platform.starter.auth.authentication;

import cn.bootx.platform.starter.auth.code.AuthLoginTypeCode;

/**
 * 用户密码认证方式
 *
 * @author xxm
 * @since 2021/7/30
 */
public interface UsernamePasswordAuthentication extends AbstractAuthentication {

    /**
     * 账密登录
     */
    @Override
    default String getLoginType() {
        return AuthLoginTypeCode.PASSWORD;
    }

}

package cn.bootx.platform.starter.auth.authentication;

import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;

/**
 * 用户状态检查
 * @author xxm
 * @since 2023/9/20
 */
public interface UserInfoStatusCheck {

    /**
     *
     * @param authInfoResult 认证返回结果
     * @param context 登录认证上下文
     */
    void check(AuthInfoResult authInfoResult, LoginAuthContext context);
}

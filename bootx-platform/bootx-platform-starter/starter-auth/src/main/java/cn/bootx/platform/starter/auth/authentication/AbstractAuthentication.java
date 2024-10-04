package cn.bootx.platform.starter.auth.authentication;

import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.hutool.extra.spring.SpringUtil;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * 抽象认证器
 *
 * @author xxm
 * @since 2021/7/30
 */
public interface AbstractAuthentication {

    /**
     * 获取终端编码
     */
    String getLoginType();
    /**
     * 获取用户状态检查接口的实现类
     */
    default UserInfoStatusCheck getUserInfoStatusCheck() {
        return SpringUtil.getBean(UserInfoStatusCheck.class);
    }

    /**
     * 登录类型是否匹配
     */
    default boolean adaptation(String loginType) {
        return Objects.equals(getLoginType(), loginType);
    }

    /**
     * 认证前操作
     */
    default void authenticationBefore(LoginAuthContext context) {

    }

    /**
     * 尝试认证, 必须重写
     */
    @NotNull
    AuthInfoResult attemptAuthentication(LoginAuthContext context);

    /**
     * 认证后处理
     */
    default void authenticationAfter(AuthInfoResult authInfoResult, LoginAuthContext context) {
    }

    /**
     * 认证流程
     */
    default AuthInfoResult authentication(LoginAuthContext context) {
        this.authenticationBefore(context);
        // 认证逻辑
        AuthInfoResult authInfoResult = this.attemptAuthentication(context);
        // 添加用户信息到上下文中
        context.setUserDetail(authInfoResult.getUserDetail());
        // 检查用户信息和状态
        this.getUserInfoStatusCheck().check(authInfoResult, context);
        // 认证后处理
        this.authenticationAfter(authInfoResult, context);
        return authInfoResult;
    }
}

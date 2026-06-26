package cn.daxpay.open.platform.capability.auth.authentication;

import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import jakarta.validation.constraints.NotNull;

/// # 认证器
///
/// 认证流程编排( before -> attempt -> 用户状态校验 -> after )由 [AuthenticationTemplate] 统一执行,
/// 本接口仅声明终端、登录方式与各步骤钩子。每个"终端( clientCode ) + 登录方式( loginType )"组合
/// 由一个具体认证器承载, [AuthenticationTemplate] 据双键路由到唯一实现。
public interface Authenticator {

    /// 终端编码(对应 [cn.daxpay.open.platform.core.enums.client.ClientEnum] 的 code)
    String getClientCode();

    /// 获取登录类型
    String getLoginType();

    /// 是否匹配终端与登录方式(双键路由)
    default boolean adaptation(String clientCode, String loginType) {
        return getClientCode().equals(clientCode) && getLoginType().equals(loginType);
    }

    /// 认证前操作
    default void authenticationBefore(LoginAuthContext context) {
    }

    /// 尝试认证, 必须重写
    @NotNull
    AuthInfoResult attemptAuthentication(LoginAuthContext context);

    /// 认证后处理
    default void authenticationAfter(AuthInfoResult authInfoResult, LoginAuthContext context) {
    }
}

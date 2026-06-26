package cn.daxpay.open.platform.capability.auth.authentication;

import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/// # 认证流程编排模板
///
/// 将"认证前 -> 尝试认证 -> 用户状态校验 -> 认证后"的模板流程从认证器接口中抽离, 统一在此编排。
/// 通过构造注入一次性解析所有 [UserInfoStatusCheck] (替代原 SpringUtil.getBeansOfType 的运行时反复查询)。
///
@Component
@RequiredArgsConstructor
public class AuthenticationTemplate {

    private final List<UserInfoStatusCheck> userInfoStatusChecks;

    /// 执行完整认证流程
    public AuthInfoResult authenticate(Authenticator authenticator, LoginAuthContext context) {
        // 认证前
        authenticator.authenticationBefore(context);
        // 尝试认证
        AuthInfoResult authInfoResult = authenticator.attemptAuthentication(context);
        // 添加用户信息到上下文中
        context.setUserDetail(authInfoResult.getUserDetail());
        // 检查用户信息和状态
        for (UserInfoStatusCheck check : userInfoStatusChecks) {
            check.check(authInfoResult, context);
        }
        // 认证后
        authenticator.authenticationAfter(authInfoResult, context);
        return authInfoResult;
    }
}

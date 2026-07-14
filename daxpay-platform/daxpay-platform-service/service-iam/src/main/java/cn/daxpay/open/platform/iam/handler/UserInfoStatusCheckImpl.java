package cn.daxpay.open.platform.iam.handler;

import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.code.UserStatusEnum;
import cn.daxpay.open.platform.capability.auth.authentication.UserInfoStatusCheck;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.capability.auth.entity.AuthInfoResult;
import cn.daxpay.open.platform.capability.auth.entity.LoginAuthContext;
import cn.daxpay.open.platform.capability.auth.exception.LoginFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/// # 用户状态检查实现类
///
@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoStatusCheckImpl implements UserInfoStatusCheck {

    /// 检查用户状态
    /// @param authInfoResult 认证返回结果
    /// @param context 登录认证上下文
    @Override
    public void check(AuthInfoResult authInfoResult, LoginAuthContext context) {
        UserDetail userDetail = authInfoResult.getUserDetail();
        PlatformStarterProperties.Auth authProperties = context.getAuthProperties();
        String clientCode = context.getClientCode();

        // 判断当前终端是否允许内置超管生效
        if (userDetail.isAdmin() && (!authProperties.isEnableAdmin()
                || !Objects.equals(ClientEnum.ADMIN.getCode(), clientCode))) {
            // 认证: 当前终端未开启内置超管登录能力
            throw new LoginFailureException("error.auth.builtinAdminLoginDisabled");
        }
        // 管理员跳过各种限制
        if (userDetail.isAdmin()) {
            return;
        }
        //  判断用户是否被禁用
        if (Objects.equals(userDetail.getStatus(), UserStatusEnum.BAN)){
            // 认证: 该用户已被禁用
            throw new LoginFailureException("error.auth.userDisabled");
        }
    }
}


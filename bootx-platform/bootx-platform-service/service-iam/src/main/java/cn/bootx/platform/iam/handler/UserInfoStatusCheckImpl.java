package cn.bootx.platform.iam.handler;

import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.iam.code.UserStatusEnum;
import cn.bootx.platform.iam.service.service.UserAdminService;
import cn.bootx.platform.starter.auth.authentication.UserInfoStatusCheck;
import cn.bootx.platform.starter.auth.configuration.AuthProperties;
import cn.bootx.platform.starter.auth.entity.AuthClient;
import cn.bootx.platform.starter.auth.entity.AuthInfoResult;
import cn.bootx.platform.starter.auth.entity.LoginAuthContext;
import cn.bootx.platform.starter.auth.exception.LoginFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户状态检查实现类
 * @author xxm
 * @since 2023/9/21
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoStatusCheckImpl implements UserInfoStatusCheck {
    private final UserAdminService userAdminService;

    /**
     *  检查用户状态
     * @param authInfoResult 认证返回结果
     * @param context 登录认证上下文
     */
    @Override
    public void check(AuthInfoResult authInfoResult, LoginAuthContext context) {
        UserDetail userDetail = authInfoResult.getUserDetail();
        AuthClient authClient = context.getAuthClient();
        AuthProperties authProperties = context.getAuthProperties();
        // 判断是否开启了超级管理员
        if (!authProperties.isEnableAdmin() && userDetail.isAdmin()) {
            throw new LoginFailureException("未开启超级管理员权限");
        }
        // 管理员跳过各种限制
        if (userDetail.isAdmin()) {
            return;
        }
        //  判断用户是否被禁用
        if (Objects.equals(userDetail.getStatus(), UserStatusEnum.BAN.getCode())){
            throw new LoginFailureException("该用户已被禁用");
        }
    }
}

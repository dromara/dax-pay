package cn.bootx.platform.starter.auth.service.impl;

import cn.bootx.platform.core.entity.UserDetail;
import cn.bootx.platform.starter.auth.configuration.AuthProperties;
import cn.bootx.platform.starter.auth.service.RouterCheck;
import cn.bootx.platform.starter.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 超级管理员跳过各种限制
 *
 * @author xxm
 * @since 2022/5/27
 */
@Component
@RequiredArgsConstructor
public class IgnoreAdminUserRouterCheck implements RouterCheck {

    private final AuthProperties authProperties;

    @Override
    public boolean check(Object handler) {
        if (authProperties.isEnableAdmin()) {
            UserDetail userDetail = SecurityUtil.getCurrentUser().orElse(new UserDetail());
            return userDetail.isAdmin();
        }
        return false;
    }

}

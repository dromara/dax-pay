package org.dromara.daxpay.platform.capability.auth.service.impl;

import org.dromara.daxpay.platform.core.entity.UserDetail;
import org.dromara.daxpay.platform.common.config.properties.PlatformStarterProperties;
import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.capability.auth.service.RouterCheck;
import org.dromara.daxpay.platform.capability.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

/// # 超级管理员跳过各种限制
///
@Component
@RequiredArgsConstructor
public class IgnoreAdminUserRouterCheck implements RouterCheck {

    private final PlatformStarterProperties platformStarterProperties;

    @Override
    public boolean check(Object handler) {
        UserDetail userDetail = SecurityUtil.getCurrentUser().orElse(new UserDetail());
        if (platformStarterProperties.getAuth().isEnableAdmin()
                && Objects.equals(ClientEnum.ADMIN.getCode(), userDetail.getClientCode())) {
            return userDetail.isAdmin();
        }
        return false;
    }

}

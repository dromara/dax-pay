package cn.daxpay.open.platform.capability.auth.service.impl;

import cn.daxpay.open.platform.core.entity.UserDetail;
import cn.daxpay.open.platform.common.config.properties.PlatformStarterProperties;
import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.capability.auth.service.RouterCheck;
import cn.daxpay.open.platform.capability.auth.util.SecurityUtil;
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

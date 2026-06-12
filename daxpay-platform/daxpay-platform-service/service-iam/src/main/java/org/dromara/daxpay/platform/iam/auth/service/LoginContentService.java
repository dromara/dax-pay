package org.dromara.daxpay.platform.iam.auth.service;

import org.dromara.daxpay.platform.core.enums.client.ClientEnum;
import org.dromara.daxpay.platform.iam.param.auth.LoginContentParam;
import org.dromara.daxpay.platform.iam.result.auth.LoginContentResult;
import org.dromara.daxpay.platform.capability.auth.authentication.AbstractAuthentication;
import org.dromara.daxpay.platform.capability.auth.exception.ApplicationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/// # 登录上下文服务
///
@Service
@RequiredArgsConstructor
public class LoginContentService {

    private final List<AbstractAuthentication> abstractAuthentications;

    /// 获取登录页上下文信息
    public LoginContentResult getLoginContent(LoginContentParam param) {
        if (param != null && param.getClientId() != null && !param.getClientId().isBlank()) {
            ClientEnum.findByCode(param.getClientId())
                    .orElseThrow(ApplicationNotFoundException::new);
        }
        return new LoginContentResult()
                .setLoginTypes(abstractAuthentications.stream()
                        .map(AbstractAuthentication::getLoginType)
                        .distinct()
                        .toList())
                .setEnableCaptcha(false)
                .setPasswordEncrypted(false);
    }

}


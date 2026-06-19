package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.param.auth.LoginContentParam;
import cn.daxpay.open.platform.iam.result.auth.LoginContentResult;
import cn.daxpay.open.platform.capability.auth.authentication.AbstractAuthentication;
import cn.daxpay.open.platform.capability.auth.exception.ApplicationNotFoundException;
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


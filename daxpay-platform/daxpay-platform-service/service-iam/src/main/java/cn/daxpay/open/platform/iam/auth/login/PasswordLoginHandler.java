package cn.daxpay.open.platform.iam.auth.login;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.auth.service.CaptchaService;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/// # 平台运营端账号密码登录
///
@Slf4j
@Component
public class PasswordLoginHandler extends AbstractPasswordLoginHandler {

    public PasswordLoginHandler(LoginRetryService loginRetryService, CaptchaService captchaService,
                                IamSecurityConfigService iamSecurityConfigService,
                                PasswordDecryptService passwordDecryptService) {
        super(loginRetryService, captchaService, iamSecurityConfigService, passwordDecryptService);
    }

    /// 平台运营端身份域编码
    @Override
    public String getClientCode() {
        return ClientEnum.ADMIN.getCode();
    }
}

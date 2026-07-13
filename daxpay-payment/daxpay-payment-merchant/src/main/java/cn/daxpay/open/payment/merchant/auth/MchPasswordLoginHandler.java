package cn.daxpay.open.payment.merchant.auth;

import cn.daxpay.open.platform.core.enums.client.ClientEnum;
import cn.daxpay.open.platform.iam.auth.login.AbstractPasswordLoginHandler;
import cn.daxpay.open.platform.iam.auth.service.CaptchaService;
import cn.daxpay.open.platform.iam.auth.service.IamSecurityConfigService;
import cn.daxpay.open.platform.iam.auth.service.LoginRetryService;
import cn.daxpay.open.platform.iam.auth.service.PasswordDecryptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/// # 商户端账号密码登录
///
/// 仅声明终端编码(merchant), 通用流程继承 [AbstractPasswordLoginHandler]。
///
@Slf4j
@Component
public class MchPasswordLoginHandler extends AbstractPasswordLoginHandler {

    public MchPasswordLoginHandler(LoginRetryService loginRetryService, CaptchaService captchaService,
                                   IamSecurityConfigService iamSecurityConfigService,
                                   PasswordDecryptService passwordDecryptService) {
        super(loginRetryService, captchaService, iamSecurityConfigService, passwordDecryptService);
    }

    /// 商户端终端编码
    @Override
    public String getClientCode() {
        return ClientEnum.MERCHANT.getCode();
    }
}

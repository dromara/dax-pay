package org.dromara.daxpay.platform.iam.auth.service;

import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.iam.code.UserStatusEnum;
import org.dromara.daxpay.platform.iam.dao.user.UserInfoManager;
import org.dromara.daxpay.platform.iam.entity.user.UserInfo;
import org.dromara.daxpay.platform.iam.exception.user.UserInfoNotExistsException;
import org.dromara.daxpay.platform.iam.param.auth.LoginSmsCaptchaSendParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 登录短信验证码服务
///
@Service
@RequiredArgsConstructor
public class LoginSmsCaptchaService {

    private final UserInfoManager userInfoManager;

    /// 发送登录短信验证码前置校验
    public void sendSmsCaptcha(LoginSmsCaptchaSendParam param) {
        UserInfo userInfo = this.findUser(param);
        if (!Objects.equals(userInfo.getStatus(), UserStatusEnum.NORMAL.getCode())) {
            throw new BizException(CommonCode.FAIL_CODE, "error.auth.auth.userStatusError");
        }
    }

    private UserInfo findUser(LoginSmsCaptchaSendParam param) {
        if (param.getClientId() != null && !param.getClientId().isBlank()) {
            return userInfoManager.findByClientCodeAndPhone(param.getClientId(), param.getPhone())
                    .orElseThrow(UserInfoNotExistsException::new);
        }
        return userInfoManager.findByPhone(param.getPhone())
                .orElseThrow(UserInfoNotExistsException::new);
    }

}

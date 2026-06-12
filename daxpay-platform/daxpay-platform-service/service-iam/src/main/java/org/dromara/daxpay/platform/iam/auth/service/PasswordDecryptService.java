package org.dromara.daxpay.platform.iam.auth.service;

import org.dromara.daxpay.platform.common.config.properties.PlatformConfigProperties;
import org.dromara.daxpay.platform.core.exception.BizException;
import org.dromara.daxpay.platform.core.util.RsaSignUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.dromara.daxpay.platform.core.code.CommonCode;

/// # 密码传输解密服务
///
/// 用于解密前端通过RSA加密传输的密码
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordDecryptService {

    private final PlatformConfigProperties platformConfigProperties;

    /// 解密密码（强制解密）
    /// @param encryptedPassword 加密后的密码
    /// @return 解密后的明文密码
    public String decryptPassword(String encryptedPassword) {
        if (StrUtil.isBlank(encryptedPassword)) {
            return encryptedPassword;
        }

        String privateKey = platformConfigProperties.getKeyConfig().getPrivateKey();
        if (StrUtil.isBlank(privateKey)) {
            throw new BizException(CommonCode.FAIL_CODE, "error.auth.auth.privateKeyNotConfigured");
        }

        try {
            return RsaSignUtil.decrypt(encryptedPassword, privateKey);
        } catch (Exception e) {
            log.error("密码解密失败: {}", e.getMessage());
            throw new BizException(CommonCode.FAIL_CODE, "error.auth.auth.passwordDecryptFailed");
        }
    }
}


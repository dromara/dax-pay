package cn.daxpay.open.platform.iam.auth.service;

import cn.daxpay.open.platform.common.config.properties.PlatformConfigProperties;
import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.util.RsaSignUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import cn.daxpay.open.platform.core.code.CommonCode;

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
            // 认证: 私钥未配置
            throw new BizException(CommonCode.FAIL_CODE, "error.auth.privateKeyNotConfigured");
        }

        try {
            return RsaSignUtil.decrypt(encryptedPassword, privateKey);
        } catch (Exception e) {
            log.error("密码解密失败: {}", e.getMessage());
            // 认证: 密码解密失败
            throw new BizException(CommonCode.FAIL_CODE, "error.auth.passwordDecryptFailed");
        }
    }
}


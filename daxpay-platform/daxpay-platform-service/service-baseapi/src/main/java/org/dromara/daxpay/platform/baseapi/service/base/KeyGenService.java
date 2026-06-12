package org.dromara.daxpay.platform.baseapi.service.base;

import org.dromara.daxpay.platform.core.rest.result.RsaKeyPairResult;
import org.dromara.daxpay.platform.core.util.RsaSignUtil;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

/// # 密钥生成服务
///
/// 提供RSA密钥对、AES密钥等通用密钥生成功能
@Service
public class KeyGenService {

    /// 生成RSA密钥对（PEM格式）
    public RsaKeyPairResult genRsaKeyPair() {
        String[] keys = RsaSignUtil.genRsaPemKey();
        return new RsaKeyPairResult(keys[0], keys[1]);
    }

    /// 生成AES通信密钥（32字节，兼容系统AES-256-GCM加密）
    public String genAesSecretKey() {
        byte[] keyBytes = new byte[24];
        new SecureRandom().nextBytes(keyBytes);
        String base64 = Base64.getEncoder().encodeToString(keyBytes);
        return base64.substring(0, 32);
    }
}

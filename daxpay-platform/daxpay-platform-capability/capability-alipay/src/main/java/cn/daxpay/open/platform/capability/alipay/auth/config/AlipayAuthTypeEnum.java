package cn.daxpay.open.platform.capability.alipay.auth.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/// # 支付宝鉴权方式
///
/// 区分公钥模式与证书模式, 决定支付宝 SDK 的接口调用方式:
/// - [KEY] 公钥模式: 使用 alipayPublicKey, 调用 `AlipayClient.execute`
/// - [CERT] 证书模式: 使用三本证书, 调用 `AlipayClient.certificateExecute`
///
@Getter
@AllArgsConstructor
public enum AlipayAuthTypeEnum {

    /// 公钥模式
    KEY("public_key"),
    /// 证书模式
    CERT("cert");

    /// 编码(与平台配置 authType 字段一致)
    private final String code;

    /// 根据编码解析, 未识别(含 null)时默认返回公钥模式
    public static AlipayAuthTypeEnum fromCode(String code) {
        if (CERT.code.equals(code)) {
            return CERT;
        }
        return KEY;
    }

    /// 是否证书模式
    public boolean isCert() {
        return this == CERT;
    }
}

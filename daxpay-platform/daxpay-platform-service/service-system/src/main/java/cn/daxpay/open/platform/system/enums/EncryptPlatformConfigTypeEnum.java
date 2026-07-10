package cn.daxpay.open.platform.system.enums;

import cn.daxpay.open.platform.core.exception.BizException;
import cn.daxpay.open.platform.core.i18n.I18nSupport;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/// # 加密平台配置类型枚举
///
/// 用于定义需要加密存储的敏感配置类型
@Getter
@RequiredArgsConstructor
public enum EncryptPlatformConfigTypeEnum implements I18nSupport {

    /// 对象存储配置
    OSS("oss"),

    /// 支付宝开放平台认证配置(appId/私钥/证书, 含敏感信息加密存储)
    ALIPAY_AUTH("alipay_auth"),

    /// 微信公众号 H5 认证配置(appId/appSecret, 含敏感信息加密存储)
    WECHAT_MP_AUTH("wechat_mp_auth"),

    /// 抖音开放平台 H5 应用认证配置(clientKey/clientSecret, 含敏感信息加密存储)
    DOUYIN_H5_AUTH("douyin_h5_auth");

    /// 编码
    private final String code;

    @Override
    public String getI18nPrefix() {
        return "enum.encrypt_platform_config_type";
    }

    /// 根据编码获取枚举
    public static EncryptPlatformConfigTypeEnum findByCode(String code) {
        return Arrays.stream(values())
            .filter(e -> e.getCode().equals(code))
            .findFirst()
            // 通用: 未知的加密平台配置类型
            .orElseThrow(() -> new BizException("error.common.enumUnknown", "EncryptPlatformConfigType"));
    }
}

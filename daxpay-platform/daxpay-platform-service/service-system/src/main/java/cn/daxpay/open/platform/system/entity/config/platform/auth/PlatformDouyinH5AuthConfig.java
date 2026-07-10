package cn.daxpay.open.platform.system.entity.config.platform.auth;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台抖音开放平台 H5 应用认证配置
///
/// 存储抖音开放平台 H5 应用凭据(clientKey/clientSecret), 用于 H5 场景授权。
/// 通过 [cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum#DOUYIN_H5_AUTH]
/// 以 AES-256-GCM 加密 JSON 存储。
///
/// 本配置独立于「三方平台登录配置」中的抖音 OAuth 登录凭据。
///
@Data
@Accessors(chain = true)
public class PlatformDouyinH5AuthConfig {

    /// 抖音开放平台 Client Key
    private String clientKey;

    /// 抖音开放平台 Client Secret
    private String clientSecret;
}

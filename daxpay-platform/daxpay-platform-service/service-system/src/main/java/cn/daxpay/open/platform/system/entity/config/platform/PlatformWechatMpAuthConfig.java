package cn.daxpay.open.platform.system.entity.config.platform;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台微信公众号 H5 认证配置
///
/// 存储微信公众号网页授权所需凭据(appId/appSecret), 用于授权登录(iam 模块)。
/// 通过 [cn.daxpay.open.platform.system.enums.EncryptPlatformConfigTypeEnum#WECHAT_MP_AUTH]
/// 以 AES-256-GCM 加密 JSON 存储。
///
@Data
@Accessors(chain = true)
public class PlatformWechatMpAuthConfig {

    /// 微信公众号 AppId
    private String appId;

    /// 微信公众号 AppSecret
    private String appSecret;
}

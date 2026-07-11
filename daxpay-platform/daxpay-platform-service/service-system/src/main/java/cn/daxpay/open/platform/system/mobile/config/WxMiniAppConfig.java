package cn.daxpay.open.platform.system.mobile.config;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信小程序应用配置(落库 JSON 形状)
///
/// 序列化后写入 [cn.daxpay.open.platform.system.entity.mobile.MobileApp#appConfig] 并加密存储。
@Data
@Accessors(chain = true)
public class WxMiniAppConfig {

    /// 小程序 AppId
    private String appId;

    /// 小程序 AppSecret
    private String appSecret;

    /// 原始 ID(gh_ 开头, 可选)
    private String originalId;
}

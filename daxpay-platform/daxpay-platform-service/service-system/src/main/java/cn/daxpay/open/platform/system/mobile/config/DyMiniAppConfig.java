package cn.daxpay.open.platform.system.mobile.config;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音小程序应用配置(落库 JSON 形状, 预留)
///
/// 序列化后写入 [cn.daxpay.open.platform.system.entity.mobile.MobileApp#appConfig] 并加密存储。
@Data
@Accessors(chain = true)
public class DyMiniAppConfig {

    /// 小程序 AppId
    private String appId;

    /// 小程序 AppSecret
    private String appSecret;
}

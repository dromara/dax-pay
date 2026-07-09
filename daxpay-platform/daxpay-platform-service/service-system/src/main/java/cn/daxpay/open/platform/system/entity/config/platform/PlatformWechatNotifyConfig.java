package cn.daxpay.open.platform.system.entity.config.platform;

import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信消息通知模板配置
///
/// 仅存储业务场景对应的模板 Id(trade/operate). 全局唯一,
/// 通过 [cn.daxpay.open.platform.system.enums.PlatformConfigTypeEnum#WECHAT_NOTIFY]
/// 以 JSON 存储于 `system_platform_config`(非加密).
///
/// 公众号 AppId/AppSecret 见 [PlatformWechatMpAuthConfig](加密配置 wechat_mp_auth).
@Data
@Accessors(chain = true)
public class PlatformWechatNotifyConfig {

    /// 交易通知模板Id(场景 trade)
    private String tradeTemplateId;

    /// 操作通知模板Id(场景 operate)
    private String operateTemplateId;
}

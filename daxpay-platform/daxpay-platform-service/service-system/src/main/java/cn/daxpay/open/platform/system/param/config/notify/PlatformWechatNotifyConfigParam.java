package cn.daxpay.open.platform.system.param.config.notify;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信消息通知模板配置参数
///
/// 仅含场景模板 Id. 公众号凭据在三方平台管理.
@Data
@Accessors(chain = true)
@Schema(title = "微信消息通知模板配置参数")
public class PlatformWechatNotifyConfigParam {

    @Schema(description = "交易通知模板Id")
    private String tradeTemplateId;

    @Schema(description = "操作通知模板Id")
    private String operateTemplateId;
}

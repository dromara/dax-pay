package org.dromara.daxpay.platform.capability.wechat.config.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "微信配置参数")
public class WechatConfigParam {

    @Schema(description = "微信公众号二维码")
    private String qrcode;

    @Schema(description = "微信公众号AppId")
    private String wxAppId;

    @Schema(description = "微信公众号AppSecret")
    private String appSecret;

    @Schema(description = "交易通知模板Id")
    private String tradeTemplateId;

    @Schema(description = "操作通知模板Id")
    private String operateTemplateId;
}

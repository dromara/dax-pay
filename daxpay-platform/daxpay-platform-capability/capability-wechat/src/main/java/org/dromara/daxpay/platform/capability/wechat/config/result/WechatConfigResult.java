package org.dromara.daxpay.platform.capability.wechat.config.result;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信配置结果")
public class WechatConfigResult extends BaseResult {

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

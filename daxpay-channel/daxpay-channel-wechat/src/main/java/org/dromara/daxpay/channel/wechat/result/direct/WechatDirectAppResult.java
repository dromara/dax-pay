package org.dromara.daxpay.channel.wechat.result.direct;

import org.dromara.daxpay.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连商户应用
///
/// 微信直连商户应用的返回结果对象，包含通道商户号、应用名称和微信应用AppId等信息。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信直连商户应用")
public class WechatDirectAppResult extends MchTradeBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "微信应用AppId")
    private String wxAppId;
}

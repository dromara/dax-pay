package cn.daxpay.open.channel.wechat.result.direct;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连商户应用
///
/// 微信直连商户应用的返回结果对象，包含通道商户号、应用名称、应用类型和微信应用AppId等信息。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信直连商户应用")
public class WechatDirectAppResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "应用类型")
    private String appType;

    @Schema(description = "微信应用AppId")
    private String wxAppId;
}

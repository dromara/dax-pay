package cn.daxpay.open.channel.wechat.result.isv;

import cn.daxpay.open.payment.common.result.MchTradeBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户应用
///
/// 微信服务商通道商户应用(子商户应用)的返回结果对象,含通道商户号、应用名称、应用类型和微信应用AppId等信息。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商通道商户应用")
public class WechatIsvMchAppResult extends MchTradeBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "应用名称")
    private String appName;

    @Schema(description = "应用类型")
    private String appType;

    @Schema(description = "微信应用AppId")
    private String wxAppId;
}

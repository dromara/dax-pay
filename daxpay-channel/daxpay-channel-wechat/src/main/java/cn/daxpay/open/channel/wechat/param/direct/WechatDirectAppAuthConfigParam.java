package cn.daxpay.open.channel.wechat.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信直连商户应用授权认证配置保存参数
///
/// 保存/更新微信直连商户应用授权认证配置时接收的请求参数，含商户号、通道商户号、应用密钥。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信直连商户应用授权认证配置保存参数")
public class WechatDirectAppAuthConfigParam {

    @NotNull(message = "{validation.field.wechatDirectAppId.notNull}")
    @Schema(description = "关联应用ID")
    private Long wechatDirectAppId;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "应用密钥")
    private String appSecret;
}

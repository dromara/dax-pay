package org.dromara.daxpay.channel.douyin.param.direct;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音直连商户应用授权认证配置保存参数
///
/// 保存/更新抖音直连商户应用授权认证配置时接收的请求参数，含商户号、通道商户号、应用密钥和授权回调地址。
///
@Data
@Accessors(chain = true)
@Schema(title = "抖音直连商户应用授权认证配置保存参数")
public class DouyinDirectAppAuthConfigParam {

    @NotNull(message = "{validation.field.douyinDirectAppId.notNull}")
    @Schema(description = "关联应用ID")
    private Long douyinDirectAppId;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Size(max = 512, message = "{validation.field.appSecret.size}")
    @Schema(description = "应用密钥")
    private String appSecret;

    @Size(max = 512, message = "{validation.field.authCallbackUrl.size}")
    @Schema(description = "授权回调地址")
    private String authCallbackUrl;
}

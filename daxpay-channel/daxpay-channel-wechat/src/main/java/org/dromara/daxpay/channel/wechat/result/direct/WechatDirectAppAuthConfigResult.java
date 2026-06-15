package org.dromara.daxpay.channel.wechat.result.direct;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信直连商户应用授权认证配置
///
/// 微信直连商户应用授权认证配置的返回结果对象，含商户号、通道商户号，以及应用密钥配置状态和授权回调地址。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信直连商户应用授权认证配置")
public class WechatDirectAppAuthConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "关联应用ID")
    private Long wechatDirectAppId;

    @Schema(description = "是否已配置应用密钥")
    private Boolean appSecretConfigured;

    @Schema(description = "授权回调地址")
    private String authCallbackUrl;
}

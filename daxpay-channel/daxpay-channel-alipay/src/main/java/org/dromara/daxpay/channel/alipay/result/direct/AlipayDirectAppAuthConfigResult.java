package org.dromara.daxpay.channel.alipay.result.direct;

import org.dromara.daxpay.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 支付宝直连商户应用授权认证配置
///
/// 支付宝直连商户应用授权认证配置的返回结果对象，含商户号、通道商户号，以及用户标识类型和授权回调地址。
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "支付宝直连商户应用授权认证配置")
public class AlipayDirectAppAuthConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "关联应用ID")
    private Long appId;

    @Schema(description = "用户标识类型")
    private String userIdType;

    @Schema(description = "授权回调地址")
    private String authCallbackUrl;
}

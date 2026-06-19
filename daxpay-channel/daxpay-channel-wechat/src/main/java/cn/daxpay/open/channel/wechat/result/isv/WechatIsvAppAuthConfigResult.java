package cn.daxpay.open.channel.wechat.result.isv;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 微信服务商应用授权认证配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商应用授权认证配置")
public class WechatIsvAppAuthConfigResult extends BaseResult {

    @Schema(description = "商户号")
    private String mchNo;

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "微信服务商应用ID")
    private Long wechatIsvAppId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "应用密钥(已脱敏)")
    private String appSecret;

    @Schema(description = "授权回调地址（仅公众号）")
    private String authCallbackUrl;
}

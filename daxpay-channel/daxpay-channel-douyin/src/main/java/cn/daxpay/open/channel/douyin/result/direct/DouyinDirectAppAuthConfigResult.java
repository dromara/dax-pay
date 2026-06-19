package cn.daxpay.open.channel.douyin.result.direct;

import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import cn.daxpay.open.payment.common.result.MchBaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 抖音直连商户应用授权认证配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "抖音直连商户应用授权认证配置结果")
public class DouyinDirectAppAuthConfigResult extends MchBaseResult {

    @Schema(description = "通道商户号")
    private String channelMchNo;

    @Schema(description = "关联应用ID")
    private Long douyinDirectAppId;

    @SensitiveInfo
    @Schema(description = "应用密钥 (已脱敏)")
    private String appSecret;

    @Schema(description = "授权回调地址")
    private String authCallbackUrl;
}

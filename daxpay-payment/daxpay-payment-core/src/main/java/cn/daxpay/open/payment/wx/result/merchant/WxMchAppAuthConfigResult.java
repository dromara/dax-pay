package cn.daxpay.open.payment.wx.result.merchant;

import cn.daxpay.open.payment.common.result.MchBaseResult;
import cn.daxpay.open.platform.common.json.sensitive.SensitiveInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 商户微信应用授权认证配置
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "商户微信应用授权认证配置")
public class WxMchAppAuthConfigResult extends MchBaseResult {

    @Schema(description = "商户微信应用ID")
    private Long wxMchAppId;

    @SensitiveInfo(front = 12, end = 12)
    @Schema(description = "应用密钥(已脱敏)")
    private String appSecret;
}

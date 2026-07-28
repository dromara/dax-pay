package cn.daxpay.open.payment.wx.param.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户微信应用授权认证配置保存参数
///
/// appSecret 为空表示不更新(由 Service 处理)。
///
@Data
@Accessors(chain = true)
@Schema(title = "商户微信应用授权认证配置保存参数")
public class WxMchAppAuthConfigParam {

    @Schema(description = "主键,新增时不传")
    private Long id;

    @NotNull(message = "{validation.field.wxMchAppId.notNull}")
    @Schema(description = "商户微信应用ID")
    private Long wxMchAppId;

    @Schema(description = "应用密钥(加密存储)，为空表示不更新")
    private String appSecret;
}

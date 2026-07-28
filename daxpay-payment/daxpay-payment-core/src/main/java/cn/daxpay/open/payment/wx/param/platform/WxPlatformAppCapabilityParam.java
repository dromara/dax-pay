package cn.daxpay.open.payment.wx.param.platform;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 平台微信应用默认能力绑定单项
///
@Data
@Accessors(chain = true)
@Schema(title = "平台微信应用默认能力绑定单项")
public class WxPlatformAppCapabilityParam {

    @NotBlank(message = "{validation.field.capability.notBlank}")
    @Schema(description = "支付能力编码")
    private String capability;

    @NotNull(message = "{validation.field.wxPlatformAppId.notNull}")
    @Schema(description = "平台微信应用ID")
    private Long wxPlatformAppId;
}

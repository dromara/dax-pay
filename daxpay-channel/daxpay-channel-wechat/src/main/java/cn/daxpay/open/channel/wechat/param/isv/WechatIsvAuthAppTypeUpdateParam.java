package cn.daxpay.open.channel.wechat.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户认证应用类型更新参数
///
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商通道商户认证应用类型更新参数")
public class WechatIsvAuthAppTypeUpdateParam {

    /// 通道商户号
    @Schema(description = "通道商户号")
    @NotBlank(message = "{validation.field.channelMchNo.notBlank}")
    private String channelMchNo;

    /// 认证应用类型(SP_APP=服务商应用, SUB_APP=子商户应用)
    /// @see cn.daxpay.open.channel.wechat.code.WechatAuthAppTypeEnum
    @Schema(description = "认证应用类型")
    @NotBlank(message = "{validation.field.authAppType.notBlank}")
    private String authAppType;
}

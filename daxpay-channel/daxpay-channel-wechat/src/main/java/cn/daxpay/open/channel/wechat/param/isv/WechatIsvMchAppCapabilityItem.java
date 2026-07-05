package cn.daxpay.open.channel.wechat.param.isv;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信服务商通道商户支付能力关联应用单项
///
/// 用于批量保存时,声明单个支付能力绑定的服务商通道商户应用(子商户应用)。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信服务商通道商户支付能力关联应用单项")
public class WechatIsvMchAppCapabilityItem {

    @NotBlank(message = "{validation.field.capability.notBlank}")
    @Schema(description = "支付能力编码")
    private String capability;

    @NotNull(message = "{validation.field.wechatIsvMchAppId.notNull}")
    @Schema(description = "关联微信服务商通道商户应用ID")
    private Long wechatIsvMchAppId;
}

package cn.daxpay.open.channel.wechat.param.direct;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信直连商户应用保存参数
///
/// 保存/更新微信直连商户应用时接收的请求参数，含商户号、通道商户号、应用名称和微信应用AppId。
///
@Data
@Accessors(chain = true)
@Schema(title = "微信直连商户应用保存参数")
public class WechatDirectAppParam {

    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键,新增时不传")
    private Long id;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.channelMerchantNo.notBlank}")
    @Schema(description = "通道商户号")
    private String channelMchNo;

    @NotBlank(message = "{validation.field.appName.notBlank}")
    @Schema(description = "应用名称")
    private String appName;

    @NotBlank(message = "{validation.field.wxAppId.notBlank}")
    @Schema(description = "微信应用AppId")
    private String wxAppId;
}

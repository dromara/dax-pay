package cn.daxpay.open.payment.wx.param.merchant;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户微信应用保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户微信应用保存参数")
public class WxMchAppParam {

    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键,新增时不传")
    private Long id;

    @NotBlank(message = "{validation.field.mchNo.notBlank}")
    @Schema(description = "商户号")
    private String mchNo;

    @NotBlank(message = "{validation.field.appName.notBlank}")
    @Schema(description = "应用名称")
    private String appName;

    @NotBlank(message = "{validation.field.appType.notBlank}")
    @Schema(description = "应用类型")
    private String appType;

    @NotBlank(message = "{validation.field.wxAppId.notBlank}")
    @Schema(description = "微信应用AppId")
    private String wxAppId;

    @NotBlank(message = "{validation.field.appSecret.notBlank}", groups = ValidationGroup.add.class)
    @Schema(description = "应用密钥，编辑时为空表示不更新")
    private String appSecret;
}

package cn.daxpay.open.payment.douyin.param.merchant;

import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 商户抖音应用保存参数
///
@Data
@Accessors(chain = true)
@Schema(title = "商户抖音应用保存参数")
public class DyMchAppParam {

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

    @NotBlank(message = "{validation.field.douyinAppId.notBlank}")
    @Schema(description = "抖音应用AppId")
    private String douyinAppId;
}

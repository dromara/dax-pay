package cn.daxpay.open.payment.douyin.param.merchant;

import cn.daxpay.open.platform.common.json.deserializer.CredentialKeepWhenBlankDeserializer;
import cn.daxpay.open.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import tools.jackson.databind.annotation.JsonDeserialize;

/// # 商户抖音应用保存参数(商户端)
///
/// 商户端专用, 不含商户号(防越权: 由控制器从 [cn.daxpay.open.payment.common.context.PaymentContext] 组装),
/// 对照运营端参数 [DyMchAppParam]。
@Data
@Accessors(chain = true)
@Schema(title = "商户抖音应用保存参数(商户端)")
public class MchDyMchAppParam {

    @NotNull(message = "{validation.field.id.notNull}", groups = ValidationGroup.edit.class)
    @Schema(description = "主键,新增时不传")
    private Long id;

    @NotBlank(message = "{validation.field.appName.notBlank}")
    @Schema(description = "应用名称")
    private String appName;

    @NotBlank(message = "{validation.field.appType.notBlank}")
    @Schema(description = "应用类型")
    private String appType;

    @NotBlank(message = "{validation.field.douyinAppId.notBlank}")
    @Schema(description = "抖音应用AppId")
    private String douyinAppId;

    @JsonDeserialize(using = CredentialKeepWhenBlankDeserializer.class)
    @NotBlank(message = "{validation.field.appSecret.notBlank}", groups = ValidationGroup.add.class)
    @Schema(description = "应用密钥，编辑时为空表示不更新")
    private String appSecret;
}

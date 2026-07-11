package cn.daxpay.open.platform.system.mobile.config.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 微信小程序应用配置入参
///
/// 敏感字段 appSecret 为空表示不更新(配合前端 diffForm)。
@Data
@Accessors(chain = true)
@Schema(title = "微信小程序应用配置参数")
public class WxMiniAppConfigParam {

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "小程序 AppId")
    private String appId;

    @Schema(description = "小程序 AppSecret(空则不更新)")
    private String appSecret;

    @Schema(description = "原始 ID(gh_ 开头, 可选)")
    private String originalId;
}

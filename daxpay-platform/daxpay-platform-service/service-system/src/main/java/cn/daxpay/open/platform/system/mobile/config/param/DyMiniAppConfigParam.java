package cn.daxpay.open.platform.system.mobile.config.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 抖音小程序应用配置入参(预留)
@Data
@Accessors(chain = true)
@Schema(title = "抖音小程序应用配置参数")
public class DyMiniAppConfigParam {

    @NotBlank(message = "{validation.field.appId.notBlank}")
    @Schema(description = "小程序 AppId")
    private String appId;

    @Schema(description = "小程序 AppSecret(空则不更新)")
    private String appSecret;
}

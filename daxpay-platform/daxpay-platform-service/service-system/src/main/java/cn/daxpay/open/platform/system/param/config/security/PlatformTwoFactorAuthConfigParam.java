package cn.daxpay.open.platform.system.param.config.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 双因素认证配置参数
///
@Data
@Accessors(chain = true)
@Schema(title = "双因素认证配置参数")
public class PlatformTwoFactorAuthConfigParam {

    @Schema(description = "是否启用双因素认证")
    private Boolean enabled;

    @Schema(description = "发行者名称")
    private String issuer;

    @Schema(description = "备用验证码数量")
    private Integer backupCodesCount;
}

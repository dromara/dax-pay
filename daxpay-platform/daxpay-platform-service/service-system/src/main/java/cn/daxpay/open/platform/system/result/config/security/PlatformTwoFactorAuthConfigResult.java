package cn.daxpay.open.platform.system.result.config.security;

import cn.daxpay.open.platform.core.result.BaseResult;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/// # 双因素认证配置结果
///
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Schema(title = "双因素认证配置结果")
public class PlatformTwoFactorAuthConfigResult extends BaseResult {

    @Schema(description = "是否启用双因素认证")
    private Boolean enabled;

    @Schema(description = "发行者名称")
    private String issuer;

    @Schema(description = "备用验证码数量")
    private Integer backupCodesCount;
}

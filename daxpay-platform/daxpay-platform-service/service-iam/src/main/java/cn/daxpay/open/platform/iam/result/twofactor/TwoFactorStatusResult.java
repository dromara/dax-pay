package cn.daxpay.open.platform.iam.result.twofactor;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 双因素认证状态
///
/// 个人中心安全设置页据此渲染不同视图(未开启/未绑定/已绑定)。
///
@Data
@Accessors(chain = true)
@Schema(title = "双因素认证状态")
public class TwoFactorStatusResult {

    @Schema(description = "平台是否启用双因素认证")
    private boolean platformEnabled;

    @Schema(description = "当前用户是否已绑定双因素认证")
    private boolean bound;

    @Schema(description = "剩余可用备用验证码数量")
    private Integer backupCodesRemaining;
}

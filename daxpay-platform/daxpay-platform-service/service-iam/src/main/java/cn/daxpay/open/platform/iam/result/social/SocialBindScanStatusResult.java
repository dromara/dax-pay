package cn.daxpay.open.platform.iam.result.social;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 扫码绑定状态
///
/// PC 端凭票据轮询公众号扫码绑定的进度.
@Data
@Accessors(chain = true)
@Schema(title = "扫码绑定状态")
public class SocialBindScanStatusResult {

    /// 票据不存在或已过期(含票据不属于当前用户的情形, 统一按不存在返回防探测)
    public static final String NOT_EXIST = "not_exist";

    /// 绑定状态: waiting 等待扫码授权 / success 绑定完成 / not_exist 票据不存在或已过期
    @Schema(description = "绑定状态(waiting/success/not_exist)")
    private String status;
}

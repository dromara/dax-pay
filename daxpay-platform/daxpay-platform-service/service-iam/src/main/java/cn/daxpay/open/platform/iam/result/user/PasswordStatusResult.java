package cn.daxpay.open.platform.iam.result.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 密码状态信息
///
@Data
@Accessors(chain = true)
@Schema(title = "密码状态信息")
public class PasswordStatusResult {

    @Schema(description = "密码是否过期")
    private Boolean expired;

    @Schema(description = "密码即将过期（剩余天数不超过提醒阈值）")
    private Boolean expiringSoon;

    @Schema(description = "密码过期时间 (UTC)")
    private OffsetDateTime expireTime;

    @Schema(description = "剩余天数(已过期返回 0), 未设置有效期返回 null")
    private Long remainingDays;

    @Schema(description = "是否初始密码")
    private Boolean initialPassword;

    @Schema(description = "平台是否启用定期轮换")
    private Boolean rotationEnabled;

    @Schema(description = "过期提醒阈值（天）")
    private Long warnDays;
}

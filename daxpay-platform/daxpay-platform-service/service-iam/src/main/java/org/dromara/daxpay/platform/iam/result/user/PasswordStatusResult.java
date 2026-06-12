package org.dromara.daxpay.platform.iam.result.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// # 密码状态信息
///
@Data
@Accessors(chain = true)
@Schema(title = "密码状态信息")
public class PasswordStatusResult {

    @Schema(description = "密码是否过期")
    private Boolean expired;

    @Schema(description = "密码即将过期（7天内）")
    private Boolean expiringSoon;

    @Schema(description = "密码过期时间")
    private LocalDateTime expireTime;

    @Schema(description = "是否初始密码")
    private Boolean initialPassword;
}

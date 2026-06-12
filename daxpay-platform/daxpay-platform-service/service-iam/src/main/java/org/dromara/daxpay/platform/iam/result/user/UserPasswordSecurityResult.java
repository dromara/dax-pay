package org.dromara.daxpay.platform.iam.result.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/// # 用户密码安全信息
///
@Data
@Accessors(chain = true)
@Schema(title = "用户密码安全信息")
public class UserPasswordSecurityResult {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "密码错误次数")
    private Integer passwordErrorCount;

    @Schema(description = "锁定结束时间")
    private LocalDateTime lockTime;

    @Schema(description = "密码过期时间")
    private LocalDateTime passwordExpireTime;

    @Schema(description = "上次修改密码时间")
    private LocalDateTime lastChangePasswordTime;

    @Schema(description = "是否初始密码")
    private Boolean initialPassword;

    @Schema(description = "上次登录失败时间")
    private LocalDateTime lastFailureTime;
}

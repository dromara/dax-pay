package cn.daxpay.open.platform.iam.result.session;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 登录锁定信息
///
@Data
@Accessors(chain = true)
@Schema(title = "登录锁定信息")
public class LoginLockResult {

    @Schema(description = "用户ID")
    private String id;

    @Schema(description = "用户名称")
    private String username;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "终端编码")
    private String clientCode;

    @Schema(description = "密码错误次数")
    private Integer passwordErrorCount;

    @Schema(description = "锁定结束时间 (UTC)")
    private OffsetDateTime lockTime;

    @Schema(description = "剩余锁定分钟数, 仅锁定中有值")
    private Long remainingMinutes;

    @Schema(description = "上次登录失败时间 (UTC)")
    private OffsetDateTime lastFailureTime;

    @Schema(description = "锁定状态: locked-锁定中 expired-已到期 counting-计数中")
    private String status;
}

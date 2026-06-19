package cn.daxpay.open.platform.iam.result.session;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

/// # 在线用户信息
///
@Data
@Accessors(chain = true)
@Schema(title = "在线用户信息")
public class OnlineUserResult {

    @Schema(description = "会话ID")
    private String sessionId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户名称")
    private String username;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "终端编码")
    private String clientCode;

    @Schema(description = "登录时间 (UTC)")
    private OffsetDateTime loginTime;
}

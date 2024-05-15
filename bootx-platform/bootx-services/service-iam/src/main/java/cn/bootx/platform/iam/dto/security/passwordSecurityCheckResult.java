package cn.bootx.platform.iam.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户密码各种状态的检查
 * @author xxm
 * @since 2023/9/25
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户密码各种状态的检查")
public class passwordSecurityCheckResult {

    @Schema(description = "默认密码修改提示")
    private boolean defaultPwd;

    @Schema(description = "密码过期")
    private boolean expirePwd;

    @Schema(description = "密码过期倒计时")
    private boolean expireRemind;

    @Schema(description = "距离过期天数")
    private int expireRemindNum;

}

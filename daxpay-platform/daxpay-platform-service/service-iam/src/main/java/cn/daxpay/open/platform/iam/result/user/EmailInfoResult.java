package cn.daxpay.open.platform.iam.result.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 邮箱绑定状态
///
@Data
@Accessors(chain = true)
@Schema(title = "邮箱绑定状态")
public class EmailInfoResult {

    @Schema(description = "绑定邮箱(未绑定为null)")
    private String email;

    @Schema(description = "邮箱是否已验证(未验证邮箱不可用于找回密码)")
    private Boolean emailVerified;
}

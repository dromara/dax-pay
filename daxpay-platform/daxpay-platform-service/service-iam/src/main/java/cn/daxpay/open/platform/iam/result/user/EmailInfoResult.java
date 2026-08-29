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
}

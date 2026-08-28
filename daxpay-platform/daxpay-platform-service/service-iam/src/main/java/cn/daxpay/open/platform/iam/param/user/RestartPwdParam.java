package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 用户密码重置
///
@Data
@Accessors(chain = true)
@Schema(title = "用户密码重置")
public class RestartPwdParam {

    @Schema(description = "用户主键不可为空")
    @NotNull(message = "{validation.field.userId.notNull}")
    private Long userId;
}

package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 修改密码参数
///
@Data
@Accessors(chain = true)
@Schema(title = "修改密码参数")
public class UpdatePasswordParam {

    @Schema(description = "原密码（RSA 加密传输）")
    @NotBlank(message = "{validation.field.oldPassword.notBlank}")
    private String password;

    @Schema(description = "新密码（RSA 加密传输）")
    @NotBlank(message = "{validation.field.newPassword.notBlank}")
    private String newPassword;
}

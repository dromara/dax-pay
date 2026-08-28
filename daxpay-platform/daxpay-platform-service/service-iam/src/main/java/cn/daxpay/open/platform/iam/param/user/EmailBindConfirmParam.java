package cn.daxpay.open.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/// # 邮箱绑定确认参数
///
@Data
@Accessors(chain = true)
@Schema(title = "邮箱绑定确认参数")
public class EmailBindConfirmParam {

    @Schema(description = "邮箱验证码")
    @NotBlank(message = "{validation.field.emailCode.notBlank}")
    private String code;
}

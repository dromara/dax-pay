package cn.bootx.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户密码重置
 * @author xxm
 * @since 2024/7/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户密码重置")
public class RestartPwdParam {

    @Schema(description = "用户主键不可为空")
    @NotNull(message = "用户不可为空")
    private Long userId;

    @Schema(description = "新密码不可为空")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}

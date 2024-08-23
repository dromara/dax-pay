package cn.bootx.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 批量用户密码重置
 * @author xxm
 * @since 2024/7/12
 */
@Data
@Accessors(chain = true)
@Schema(title = "批量用户密码重置")
public class RestartPwdBatchParam {

    @Schema(description = "用户主键集合集合")
    @NotEmpty(message = "用户不可为空")
    private List<Long> userIds;
    @Schema(description = "新密码不可为空")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}

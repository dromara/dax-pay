package org.dromara.daxpay.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/// # 批量用户密码重置
///
@Data
@Accessors(chain = true)
@Schema(title = "批量用户密码重置")
public class RestartPwdBatchParam {

    @Schema(description = "用户主键集合集合")
    @NotEmpty(message = "{validation.field.userIds.notEmpty}")
    private List<Long> userIds;
    @Schema(description = "新密码不可为空")
    @NotBlank(message = "{validation.field.newPassword.notBlank}")
    private String newPassword;
}

package cn.daxpay.open.platform.iam.param.upms;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
@Schema(title = "用户角色参数")
public class UserRoleParam {

    @Schema(description = "用户的ID")
    @NotNull(message = "{validation.field.userId.notNull}")
    private Long userId;

    @Schema(description = "角色的ID")
    @NotNull(message = "{validation.field.roleId.notNull}")
    private Long roleId;

}

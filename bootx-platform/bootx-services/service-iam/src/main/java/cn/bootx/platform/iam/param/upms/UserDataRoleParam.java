package cn.bootx.platform.iam.param.upms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * @author xxm
 * @since 2022/1/2
 */
@Data
@Schema(title = "用户数据权限参数")
public class UserDataRoleParam {

    @Schema(description = "用户的ID", requiredMode = REQUIRED)
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "数据角色ID", requiredMode = REQUIRED)
    private Long dataRoleId;

}

package cn.daxpay.open.platform.iam.param.upms;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(title = "用户角色批量分配参数")
public class UserRoleBatchParam {

    @Schema(description = "用户的ID集合")
    @NotEmpty(message = "{validation.field.userIds.notEmpty}")
    private List<Long> userIds;

    @Schema(description = "角色的ID")
    @NotNull(message = "{validation.field.roleId.notNull}")
    private Long roleId;

}

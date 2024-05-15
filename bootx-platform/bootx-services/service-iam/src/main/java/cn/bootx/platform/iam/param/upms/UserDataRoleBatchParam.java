package cn.bootx.platform.iam.param.upms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * @author xxm
 * @since 2022/6/7
 */
@Data
@Schema(title = "用户数据权限批量设置参数")
public class UserDataRoleBatchParam {

    @Schema(description = "用户的ID", requiredMode = REQUIRED)
    @NotEmpty(message = "用户集合不能为空")
    private List<Long> userIds;

    @Schema(description = "数据角色ID", requiredMode = REQUIRED)
    private Long dataRoleId;

}

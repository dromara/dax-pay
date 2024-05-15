package cn.bootx.platform.iam.param.upms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xxm
 * @since 2020/5/1 18:10
 */
@Data
@Schema(title = "用户角色参数")
public class UserRoleParam {

    @Schema(description = "用户的ID", required = true)
    @NotNull(message = "用户 ID 不能为空")
    private Long userId;

    @Schema(description = "角色的ID集合", required = true)
    @NotNull(message = "roleIds 不能为空")
    private List<Long> roleIds;

}

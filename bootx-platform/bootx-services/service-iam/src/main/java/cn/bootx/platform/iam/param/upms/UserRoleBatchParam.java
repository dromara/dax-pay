package cn.bootx.platform.iam.param.upms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xxm
 * @since 2020/5/1 18:10
 */
@Data
@Schema(title = "用户角色参数")
public class UserRoleBatchParam {

    @Schema(description = "用户的ID", required = true)
    @NotEmpty(message = "用户集合不能为空")
    private List<Long> userIds;

    @Schema(description = "角色的ID集合", required = true)
    private List<Long> roleIds;

}

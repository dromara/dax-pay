package cn.bootx.platform.iam.param.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author xxm
 * @since 2021/9/29
 */
@Data
@Accessors(chain = true)
@Schema(title = "用户部门批量关联")
public class UserDeptBatchParam {

    @Schema(description = "用户id")
    @NotEmpty(message = "用户id不可为空")
    private List<Long> userIds;

    @Schema(description = "部门id集合")
    private List<Long> deptIds;

}

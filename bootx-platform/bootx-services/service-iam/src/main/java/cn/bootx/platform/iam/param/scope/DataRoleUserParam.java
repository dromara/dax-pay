package cn.bootx.platform.iam.param.scope;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author xxm
 * @since 2021/12/24
 */
@Data
@Accessors(chain = true)
@Schema(title = "部门范围")
public class DataRoleUserParam {

    @Schema(description = "数据角色id")
    private Long dataRoleId;

    @Schema(description = "用户id")
    private List<Long> userIds;

}

package cn.bootx.platform.iam.param.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author xxm
 * @since 2021/6/17
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色")
public class RoleParam {

    @Schema(description = "角色id")
    private Long id;

    @Schema(description = "父级Id")
    private Long pid;

    @Schema(description = "角色code")
    private String code;

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "描述")
    private String remark;

}

package cn.bootx.platform.iam.param.role;

import cn.bootx.platform.core.validation.ValidationGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(groups = { ValidationGroup.edit.class }, message = "主键不可为空")
    @Schema(description = "角色id")
    private Long id;

    @Schema(description = "父级Id")
    private Long pid;

    @NotBlank(message = "编码不可为空")
    @Schema(description = "角色code")
    private String code;

    @NotBlank(message = "角色名称不可为空")
    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "描述")
    private String remark;

}

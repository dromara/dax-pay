package cn.bootx.platform.iam.param.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 菜单分配参数
 * @author xxm
 * @since 2024/7/5
 */
@Data
@Accessors(chain = true)
@Schema(title = "菜单分配参数")
public class PermMenuAssignParam {

    /** 角色ID */
    @NotNull(message = "角色ID不可为空")
    @Schema(description = "角色ID")
    private Long roleId;
    /** 终端编码 */
    @NotBlank(message = "终端编码不可为空")
    @Schema(description = "终端编码")
    private String clientCode;
    /** 菜单ID */
    @Schema(description = "菜单ID")
    private List<Long> menuIds;
    /** 是否更新子孙角色 */
    @Schema(description = "是否更新子孙角色")
    private boolean updateChildren;
}

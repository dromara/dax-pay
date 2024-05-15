package cn.bootx.platform.iam.param.upms;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author xxm
 * @since 2021/6/9
 */
@Data
@Accessors(chain = true)
@Schema(title = "角色权限(菜单/权限码/请求)")
public class RolePermissionParam implements Serializable {

    private static final long serialVersionUID = 4344723093613370279L;

    @Schema(description = "角色的ID")
    @NotNull(message = "角色不可为空")
    private Long roleId;

    @Schema(description = "终端code")
    @NotBlank(message = "终端不可为空", groups = {PermMenu.class})
    private String clientCode;

    @Schema(description = "权限id")
    private List<Long> permissionIds;

    @Schema(description = "新增时是否更新子节点")
    private boolean updateChildren;

    /**
     * 菜单权限需要终端, 请求权限不需要
     */
    public interface PermMenu {}

}

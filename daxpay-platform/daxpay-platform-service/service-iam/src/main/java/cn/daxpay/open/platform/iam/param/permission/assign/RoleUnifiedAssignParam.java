package cn.daxpay.open.platform.iam.param.permission.assign;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 角色统一授权参数
///
/// 一次请求里会同时提交菜单授权与权限码授权结果，服务端再分别写入菜单关系和权限码关系。
@Data
@Accessors(chain = true)
@Schema(title = "角色统一授权参数")
public class RoleUnifiedAssignParam {

    @NotNull(message = "{validation.field.roleId.notNull}")
    @Schema(description = "角色ID")
    private Long roleId;

    @NotBlank(message = "{validation.field.clientCode.notBlank}")
    @Schema(description = "身份域编码")
    private String clientCode;

    @Schema(description = "菜单ID列表")
    private List<Long> menuIds = new ArrayList<>();

    @Schema(description = "权限码ID列表")
    private List<Long> codeIds = new ArrayList<>();

    @Schema(description = "是否更新子孙角色")
    private boolean updateChildren;
}

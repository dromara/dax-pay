package cn.daxpay.open.platform.iam.result.permission.assign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 角色统一授权树节点
///
/// 统一授权树同时承载菜单节点与权限码节点，两类节点通过 `type` 字段区分。
/// 为支持菜单节点与权限码节点混合构树，新增 `treeId` 与 `treePid` 作为专用树构建主键：
/// 菜单 `menu-{menuId}`，权限码 `code-{codeId}-menu-{menuId}`（同一权限码可挂多个同 menuCode 菜单时保证 key 唯一）。
@Data
@Accessors(chain = true)
@Schema(title = "角色统一授权树节点")
public class RoleUnifiedAssignTreeResult {

    @Schema(description = "节点key，菜单 menu-{menuId}，权限码 code-{codeId}-menu-{menuId}")
    private String key;

    @Schema(description = "节点类型 menu/code")
    private String type;

    @Schema(description = "树构建主键，菜单节点使用 menu-{menuId}，权限码节点使用 code-{codeId}-menu-{menuId}（一码多挂时保证唯一）")
    private String treeId;

    @Schema(description = "树构建父主键，菜单节点指向父菜单树主键，权限码节点指向所属菜单树主键")
    private String treePid;

    @Schema(description = "菜单ID")
    private Long id;

    @Schema(description = "父菜单ID")
    private Long pid;

    @Schema(description = "权限码ID")
    private Long codeId;

    @Schema(description = "权限码编码")
    private String code;

    @Schema(description = "国际化key")
    private String i18nKey;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "身份域编码")
    private String clientCode;

    @Schema(description = "菜单类型")
    private String menuType;

    @Schema(description = "排序")
    private Double sortNo;

    @Schema(description = "子节点")
    private List<RoleUnifiedAssignTreeResult> children = new ArrayList<>();

    public Double getSortNo() {
        return sortNo == null ? 0D : sortNo;
    }
}

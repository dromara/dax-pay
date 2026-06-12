package org.dromara.daxpay.platform.iam.result.permission.assign;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/// # 角色统一授权树节点
///
/// 统一授权树同时承载菜单节点与权限码节点，两类节点通过 `type` 字段区分。
/// 为支持菜单节点与权限码节点混合构树，新增 `treeId` 与 `treePid` 作为专用树构建主键，
/// 避免菜单 ID 与权限码 ID 数值冲突导致的节点挂载错误。
@Data
@Accessors(chain = true)
@Schema(title = "角色统一授权树节点")
public class RoleUnifiedAssignTreeResult {

    @Schema(description = "节点key")
    private String key;

    @Schema(description = "节点类型 menu/code")
    private String type;

    @Schema(description = "树构建主键，菜单节点使用 menu-{menuId}，权限码节点使用 code-{codeId}")
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

    @Schema(description = "权限码中文名称")
    private String nameCn;

    @Schema(description = "权限码英文名称")
    private String nameEn;

    @Schema(description = "菜单编码")
    private String menuCode;

    @Schema(description = "终端编码")
    private String clientCode;

    @Schema(description = "标题-中文")
    private String titleCn;

    @Schema(description = "标题-英文")
    private String titleEn;

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
